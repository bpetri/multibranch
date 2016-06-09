node() {


  //fewf2
  checkout scm

  def han = 'han'
  def solo = 'solo'

  stage 'simple echo'
  echo han + ' ' + solo

  sh 'ls'

  sh 'sleep 30'

  // check if there is a newer commit
   sh 'git fetch origin ${env.BRANCH_NAME} > GIT_FETCH'
   fetch=readFile('GIT_FETCH')

   echo "----"
   echo fetch
   echo "----"
   echo "" + fetch.length()

//  if (env.BRANCH_NAME == 'master')
//  sh 'sleep 7200'


/*
#  pipeline = load 'test.groovy'
#  pipeline.deploy('lea')

#  if (env.BRANCH_NAME == 'master')
#     build job: '/integration-test.develop', wait: false
*/
}
