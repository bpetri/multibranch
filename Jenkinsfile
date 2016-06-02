node() {

  def han = 'han'
  def solo = 'solo'

  stage 'simple echo'
  echo han + ' ' + solo

  pipeline = load 'test.groovy'
  pipeline.deploy('lea') 
}
