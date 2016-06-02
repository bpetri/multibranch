node() {

  checkout scm

  def han = 'han'
  def solo = 'solo'

  stage 'simple echo'
  echo han + ' ' + solo

  sh 'ls'

  pipeline = load 'test.groovy'
  pipeline.deploy('lea')

  if (env.BRANCHNAME == 'develop')
     build job: '/integration-test.develop', wait: false
}
