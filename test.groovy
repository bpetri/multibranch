


def deploy(componentname) {
  stage 'Dffeploy ' + componentname
  sh 'echo  ' + componentname + ' release'

  def envStr = "TEST_STR=ThisisJob${env.JOBNAME}andComponent".concat(componentname); 

  withEnv([envStr]) {
    sh 'echo hallo'
    sh 'echo $TEST_STR'
  }
}

return this;
