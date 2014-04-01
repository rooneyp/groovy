package com.rooney

//Note: Methods and classes are public by default.
//		single quote strings

for(i in 0..2) { print 'ho ' }
println 'Merry Groovy!'

// The upto() method accepts a closure as a parameter. 
//If the closure expects only one parameter, we can use the default name it for it in Groovy.
print 'loop with upto: '
0.upto(2) { print "$it "} //$ gives param sub

print '\niterate with times: '
3.times { print "$it "}

print '\niterate with step: '
0.step(10, 2) { print "$it "}


//use 'execute' to run a command
println "\n"
//println "svn help".execute().text //this is calling getText
println "svn help".execute().getClass().name  //calls getName


//safe navigation operator ?.
def safeNavigationOperator(str) {
	//if (str != null) { str.reverse() } 
	str?.reverse() //note we do need a final return
}
println safeNavigationOperator('evil')
println safeNavigationOperator(null)


//Checked Exceptions treated as runtime: ignore or catch
def openFile(fileName) {
	new FileInputStream(fileName)
}
//openFile("nonexistentfile")

try { 
	openFile("nonexistentfile")
} catch(ex) { //no type so catch All Exs (not Error/Throwable)
	println "Oops: " + ex
}
//catch(FileNotFoundException ex) { //specific
//	println "Oops: " + ex
//}


//static method can return this to ref the 'class'
class Wizard {
	def static learn(trick, action) {
		print "learning $trick : $action "
		this 
	}
}
Wizard.learn('alohomora', 'xxx').learn('expelliarmus', 'yyy').learn('lumos', 'zzz')


//Javabeans are well supported
//when create a property, Groovy quietly creats a getter and a setter method behind the scenes
class Car {
	def miles = 0 //def is needed.
	final year    //final is needed. Simply creates a read-only property 
	String colour    //type is required. no default assignment needed
	private privateVar; 
	Car(theYear) { year = theYear }
}
Car car = new Car(2008)
println "Year: $car.year Miles: $car.miles colour: $car.colour" 
car.miles = 25
car.colour = 999
car.privateVar = "foo" //get set thru groovy auto generated setter. disable by implementing explicit noop setter
//car.year = 2000 //groovy.lang.ReadOnlyPropertyException: Cannot set readonly property: year for class: com.rooney.Car
println "Year: $car.year Miles: $car.miles colour: $car.colour" 


//use getClass instead of .class as Maps/builders are special
str = 'hello'
println "string type is " + str.class.name + "\n"
//println "var sub $str $str.getClass().name" //groovy.lang.MissingPropertyException: No such property: getClass for class: java.lang.String


//2.3 Flexible Initialization and Named Arguments
class Robot {
	def type, height, width
	def access(location, weight, fragile) {
		println "Received fragile? $fragile, weight: $weight, loc: $location"
	}
}
robot = new Robot(type: 'arm', height: 40, width: 10)  //'flexible constructor' created by groovy. any order supported
println "$robot.type, $robot.height, $robot.width"
robot.access(x: 30, y: 20, z: 10, 50, true) //map type auto detected
robot.access(x: 30, y: 20, z: 10, true, 50) //fragile/weight not interpreted
robot.access(50, true, x: 30, y: 20, z: 10) //


//Optional parameters. if param given a default value. Array is assigned as empty array
def log(x, base=10, String[] details) { 
	print " details: " + details + " "
	Math.log(x) / Math.log(base)
}

println log(1024) + " " + log(1024, 2) + " " + log(1024, 2, '123-456-7890') + " " + log(1024, 2, '123-456-7890', '231-546-0987')







