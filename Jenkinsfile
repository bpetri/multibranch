node() {
  // xwef
  checkout scm

  def han = 'han'
  def solo = 'solo'

  stage 'simple echo'
  echo han + ' ' + solo


println "CAUSE ${currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause).properties}"

def cause = currentBuild.rawBuild.getCause(Cause.UserIdCause)

  echo "---"
  echo " " + cause
  echo "----"
  echo " " + cause.userId

  sh 'ls'

  sh 'sleep 30'

  // check if there is a newer commit

sh('git rev-parse HEAD > GIT_COMMIT')
git_commit=readFile('GIT_COMMIT')

  echo "git comit is" + git_commit
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


/*
#  pipeline = load 'test.groovy'
#  pipeline.deploy('lea')

#  if (env.BRANCH_NAME == 'master')
#     build job: '/integration-test.develop', wait: false
*/
}
