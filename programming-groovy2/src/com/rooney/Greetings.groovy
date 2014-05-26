package com.rooney

import java.awt.event.ActionListener
import java.awt.event.FocusListener
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane

// /Users/paul/Dev/books/Programming Groovy 2.pdf
//Methods and classes are public by default.
//single quote strings
//String sub using $xxx
//[A:1, B:2] for map
//[A,B,C] for list
// 'as' operator to supply closure or map of closures for interface impl 
// {xxx} curly brackets for closures
// Strings are evaluated before executed and can contain statements: "System.out".println("xx")
//
/* Booleans - implementing an asBoolean() method in our classes.
Boolean 		True
Collection 		Not empty
Character 		Value not 0
CharSequence 	Length greater than 0
Enumeration 	Has more elements
Iterator 		Has next
Number			Double value not 0
Map				Not empty
Matcher			At least one match
Object[]		Length greater than 0
Any other type	Reference not null
*/

print 'loop with in: '
for(i in 0..2) { print 'ho ' }
println 'Merry Groovy!'

// The upto() method accepts a closure as a parameter. 
//If the closure expects only one parameter, we can use the default name 'it' for it in Groovy.
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

println log(1024) + " " + log(1024, 2) + " " + log(1024, 2, '123-456-7890') + " " + log(1024, 2, '123-456-7890', '231-546-0987') + "\n"



//2.5 Using Multiple Assignments:  return an array and use comma-separated variables wrapped in parentheses on the left side of the assignment.
def splitName(fullName) { fullName.split(' ') }
(firstName, lastName) = splitName('James Bond')
println "$lastName, $firstName $lastName"

//swap fields using this
def name1 = "AAA" 
def name2 = "BBB"
(nameBAR, nameFOO) = [name1, name2] 
println "$nameFOO and $nameBAR"

//it handle excess and missing vars
(nameA) = [name1, name2]
(nameY, nameX) = [name2]
println "$nameX  $nameY"  //nameX is null. we get a 

//can't handle missing vars if using primitives
//int intX, intY
//(intY, intX) = [1]
//println "$intX, $intY" org.codehaus.groovy.runtime.typehandling.GroovyCastException: Cannot cast object 'null' with class 'null' to class 'int'. Try 'java.lang.Integer' instead



//2.6 Implementing Interfaces
//we don't have to impl all methods on i/f => this is good for Mocking
//use 'as' operator if we know type at compile time
new JButton().addActionListener( 
	{ JOptionPane.showMessageDialog(null, "You clicked!") } as ActionListener //no need for anon class impl and method - The i/f has a single method
)

//one close for many many methods on many interfaces
displayMouseLocation = { positionLabel.setText("$it.x, $it.y") } //$it is var for where there is a single param to a closure
new JFrame().addMouseListener(displayMouseLocation as MouseListener) //provide impl for every method
new JFrame().addMouseMotionListener(displayMouseLocation as MouseMotionListener) //provide impl for every method

//supply impl for multiple methods using map of closures, keyed by method name
handleFocusMap = [
	focusGained : { msgLabel.setText("Good to see you!") }, 
	focusLost : { msgLabel.setText("Come back soon!") }
]
new JButton().addFocusListener(handleFocusMap as FocusListener)

// use 'aClosure.asType()' to set impl for classtype only known at runtime
events = ['WindowListener', 'ComponentListener'] // Above list may be dynamic and may come from some input 
handlerClosure = { msgLabel.setText("$it") }
println "closure class is " + handlerClosure.getClass().name
for (event in events) {
	handlerImpl = handlerClosure.asType(Class.forName("java.awt.event.${event}"))
	new JFrame()."add${event}"(handlerImpl) 
}

"System.out".println("code statement can be built in a String")


//2.7 Groovy Boolean Evaluation
//implementing an asBoolean() method in our own classes.
if ('hello') { println 'hello' }
if (null) { println 'null' }
if (true) { println 'true' }
if (false) { println 'false' }
if (1) { println '1' }
if (0) { println '0' }
println 'empty list treated as ' + ([] ? 'true' : 'false') 


//2.8 Operator Overloading
for(ch = 'a'; ch < 'd'; ch++) { 
	print ch + ","
}

// '<<' is mapped to leftShift() groovy method on Collections
lst = ['\nhello'] 
lst << 'there' 
println lst


class ComplexNumber { 
	def real, imaginary 
	
	def plus(other) { // overrides '+' for an Object type
		new ComplexNumber(real: real + other.real, imaginary: imaginary + other.imaginary) //using default groovy ctor with map of fieldname:values
	}
	
	String toString() { 
		"$real ${imaginary > 0 ? '+' : ''} ${imaginary}i"
	} 
}

c1 = new ComplexNumber(real: 1, imaginary: 2)
c2 = new ComplexNumber(real: 4, imaginary: 1) 
println "override +: " + (c1 + c2)


//2.9 Support of Java 5 Language Features
//Groovy automatically treats primitives as objects where necessary
//Primitives are treated as objects only where necessary—for instance, 
//if we invoke methods on them or pass them to object references. Otherwise, Groovy retains them as primitive types at the bytecode level.
def int i = 5
println i.getClass().name

