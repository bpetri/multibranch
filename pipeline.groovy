
def job_unit_tests(performPull=true) {
    build_cypp_one(performPull)
    perform_all_unit_tests()
    remove_cypp_one()
}


def job_integration_tests(performUnitTest=true) {
    if (performUnitTest)
      job_unit_tests()
    build_cypp_one()
    deploy_all_components(["frontend", "cve-importer"])
    perform_all_integration_test()
    remove_cypp_one()
}


/*************************/

def build_cypp_one(performPull=false) {
    stage 'Building cypp one'
    if (performPull)
      sh 'git pull'
    sh 'docker build -t cypp/one .'
}

def remove_cypp_one() {
    sh 'docker rmi -f cypp/one'
}


def check_for_git_changes(branchName) {
  sh 'rm -f GIT_DIFF.TMP'
  sh ('git fetch && git diff --name-only HEAD origin/' + branchName +  ' > GIT_DIFF.TMP')
  gdt=readFile('GIT_DIFF.TMP')

  return (gdt.length() > 0)
}


def release_all_components(componentarray) {
    for (String componentname : componentarray) {
        build_cypp_one(true)
        release(componentname)
        remove_cypp_one()
    }
}

def release(componentname) {
    stage 'Release ' + componentname
    sh 'docker run --rm  cypp/one /opt/cypp/ONe/execute.sh ' + componentname + ' release'
}


def deploy_all_components(componentarray) {
    for (String componentname : componentarray) {
        deploy(componentname)
    }
}

def deploy(componentname) {
    stage 'Deploy ' + componentname
    sh 'docker run --rm  cypp/one /opt/cypp/ONe/execute.sh ' + componentname + ' deploy-snapshot'
}

def perform_all_unit_tests() {
    stage 'Perform unit-tests'
    parallel(frontend: {
        perform_unit_test('frontend')
        }, cveimporter: {
        perform_unit_test('cve-importer')
        }, ocsimporter: {
        perform_unit_test('ocs-importer')
        }
    )
}

def perform_unit_test(componentname) {
    withEnv(["RESULT_DIR=/opt/jenkins-results/${env.JOB_NAME}/${env.BUILD_NUMBER}/unit-test/" + componentname]) {
            sh 'mkdir -p ${RESULT_DIR}'
            try {
              sh 'docker run --rm -v ${RESULT_DIR}:/test-results cypp/one /opt/cypp/ONe/execute.sh ' + componentname + ' test2junit'
            }
            finally {
              sh 'rm -rf results_unittest_' + componentname + '; mkdir results_unittest_' + componentname;
              sh 'cp -R ${RESULT_DIR}/xml results_unittest_' + componentname;
              step([$class: 'JUnitResultArchiver', testResults: 'results_unittest_' + componentname + '/xml/*.xml'])
            }
         }
}

def startup_test_stack() {
    stage 'Start test stack'
    sh 'docker-compose -f test/docker-compose/docker-compose-fullstack.yml up -d'
}

def shutdown_test_stack() {
    stage 'Shutdown test stack'
    sh 'docker-compose -f test/docker-compose/docker-compose-fullstack.yml -f test/docker-compose/docker-compose-chrome.yml -f test/docker-compose/docker-compose-firefox.yml stop'
    sh 'docker-compose -f test/docker-compose/docker-compose-fullstack.yml -f test/docker-compose/docker-compose-chrome.yml -f test/docker-compose/docker-compose-firefox.yml rm -f'
}

def perform_all_integration_test() {
    stage 'Perform integration-test'

    // ensure that test stack is not running anymore
    shutdown_test_stack()
    startup_test_stack()
    try {
      parallel(
          chrome: {
              perform_integration_test('chrome')
          },
          firefox: {
              perform_integration_test('firefox')
          }
      )
    }
    finally {
      shutdown_test_stack()
    }
}

def perform_integration_test(browsername) {
   withEnv(["RESULT_DIR=/opt/jenkins-results/${env.JOB_NAME}/${env.BUILD_NUMBER}/" + browsername]) {
                sh 'mkdir -p ${RESULT_DIR}'
                try {
                  sh 'docker-compose -f test/docker-compose/docker-compose-fullstack.yml -f test/docker-compose/docker-compose-' + browsername + '.yml run --rm integration-test-' + browsername
                }
                finally {
                  sh 'rm -rf results_' + browsername + '; mkdir results_' + browsername
                  sh 'cp -R ${RESULT_DIR}/xml results_' + browsername + ' && cd results_' + browsername + '/xml && for i in *.xml; do mv $i ' + browsername + '.$i; done && sed -i "s/classname=\\"/classname=\\"' + browsername + './g" *.xml'
                  step([$class: 'JUnitResultArchiver', testResults: 'results_' + browsername + '/xml/*.xml'])
                }
    }
}

return this;
