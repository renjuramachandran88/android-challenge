# Theoretical Questions

In order to proceed with the interviewing process T-Pro asked a couple of theoretical questions that
are splitted by two levels.

## First Level

**1. Explain the main concept of lambda functions.**

A: Lamda functions can be defined as a small functions without a name. Lambda function can be used
to save simple expression in variable. For example in kotlin

```
val list = listOf(1,2,3,4,5,6,7,8)
val evenList = list.filter{it%2 ==0 }
```

In the above example filter{} is a lambda function where it filters the even numbers from the list.

Lambdas can accept multiple values as well

```
fun main() {
val calculateArea = {width: Int, height: Int ->
if(height <= 0 || width<=0) {
"Invalid values, Please use positive integers"
} else {
width*height
}

val width = 3
val height = 5
val area = calculateArea(3, 5)

}
```

**2. What's the difference between `UInt16` and `Int32`?**

A: UInt16 represents unsigned 16-bit int data type. It can only non-negative integers from 0 to
almost 2^16.
Int32 represents unsigned 32-bit int data type. As this value is unsigned it can contain both
positive and and negative value ranging from -2^32 to 2^32

**4. Do you have any experience with functional programming languages?**

A: Yes, I have used functional programming concepts in Kotlin language. Like lambda functions, higher order function,
collection operations, extension function.

examples:
Lambdas
```
val modifyStringValue = { it: String -> "$it!!!" }
val stringValue = modifyStringValue("Hello")
```
Extension function
```
fun String.modifyStringValue(): String {
    return "$this!!!"
}

fun main() {
val value = "hello".modifyStringValue()
println(value)
}
```
Sealed class with when expression
```
sealed class Result {
data class Success(val successMessage: String): Result()
object Failure: Result()
}

fun main() {
val result = Result.Success("Successful")
when(result) {
 is Result.Success -> println(result.successMessage)
 is Result.Failure -> println("error")
 }
}
```

## Second Level

**1. Do you have experience with C++ (JNI) code in Android projects? If yes describe what you have
implemented.**

A: No, I don't have much experience in JNI. 