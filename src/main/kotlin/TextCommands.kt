import androidx.compose.ui.text.capitalize
import java.util.*

class TextCommands {

}

enum class textCommand{
    removeAll,replace, add, enumerate,removeAmount, lowercase, uppercase,capitalize,removeUntil
}

fun removeAll(fileName: String) : String
{
    return "";
}
fun replace(fileName: String,replaceFrom: String, replaceTo: String) : String
{
    return fileName.replace(replaceFrom,replaceTo);
}

fun add(fileName: String,toAdd: String,modifier: String) : String
{
    if(modifier == "End") {
        return fileName + toAdd;
    }
    return toAdd+fileName;
}

fun enumerate(fileName: String,index: Int,modifier: String): String
{
    // TODO Add so that if many is modified it will be called as file-001 instead of file-1
    if(modifier == "Start") {
        return  index.toString()+fileName;
    }
    return fileName+index.toString();
}

fun lowercase(fileName: String): String
{
    return fileName.lowercase()
}
fun uppercase(fileName: String): String
{
    return fileName.uppercase()
}

fun capitalize(fileName: String,modifier: String): String
{
    return fileName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun removeAmount(fileName: String,amount: Int,modifier: String): String
{
    // TODO
    return fileName
}

fun removeUntil(fileName: String,amount: Int,modifier: String): String
{
    // TODO
    return fileName
}