node('build-01') {

  pipeline = load 'pipeline.groovy'
  author = ''


    try {
      stage 'Checkout'
        checkout scm

        // get user
        sh('git log --pretty=%cn -1 > GIT_AUTHOR')
        author=readFile('GIT_AUTHOR')

        echo 'last commit from '+ author
    }
  catch(error) {
    echo  "Jep, something got wrong again - the build broke: ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
      throw error
  }

  if (author != 'cy-bot') {
    try {
      echo "perform unit-tests"

     //  pipeline.job_unit_tests(false)
    }
    catch(error) {
echo "Jep, something got wrong again - the build broke: ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
        throw error
    }

    try {
      if (pipeline.check_for_git_changes(env.BRANCH_NAME) == false ) {

          echo "NO CHANGES WERE DONE"
 
      }

      echo "CHECK FOR GIT CHANGES DONE"
    }
    catch(error) {
        echo "error while checking for git changes"
        throw error
    }




        checkout scm

          def han = 'han'
          def solo = 'solo'

          stage 'simple echo'
          echo han + ' ' + solo



          sh 'rm -f GIT_FETCH'
          sh ( 'git fetch && git diff --name-only HEAD origin/${BRANCH_NAME} > GIT_DF')

          fdf=readFile('GIT_DF')

          echo "----"
          echo "fethc is "+ fdf
          echo "----"
          echo "" + fdf.length()


          if (fdf.length() == 0) {
            sh 'git checkout ${BRANCH_NAME}'
          }
          else {
            echo "Sorry there was a commit while trying to release - we are not releasing"
          }


        //  if (env.BRANCH_NAME == 'master')
        //  sh 'sleep 7200'


      }
}
