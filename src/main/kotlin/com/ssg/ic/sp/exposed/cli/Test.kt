import kotlin.reflect.KProperty

class CustomString {
    private var value = ""

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = "[CustomString] $value"

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        this.value = value
    }
}

fun main() {
    var customString by CustomString()
    customString = ""
    println(customString)
}