

def deploy(componentname) {
  stage 'Deploy ' + componentname
  sh 'echo  ' + componentname + ' release'
  withEnv(["TEST_STR=ThisisJob${env.JOBNAME}andComponent" + component]) {
    sh 'echo hallo'
    sh 'echo $TEST_STR'
  }
}

return this;
