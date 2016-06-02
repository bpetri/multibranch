

def deploy(componentname) {
   stage 'Deploy ' + componentname
    sh 'echo  ' + componentname + ' release'
}

return this;
