

def deploy(componentname) {
  stage 'Deploy ' + componentname
  sh 'echo  ' + componentname + ' release'

  def envStr = "TEST_STR=ThisisJob${env.JOBNAME}andComponent" + componentname; 

  withEnv([envStr]) {
    sh 'echo hallo'
    sh 'echo $TEST_STR'
  }
}

return this;
