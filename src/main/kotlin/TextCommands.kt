import androidx.compose.ui.text.capitalize
import java.util.*

class TextCommands {

}

enum class textCommand{
    removeAll,replace, add, enumerate,removeAmount, lowercase, uppercase,capitalize
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
    if(modifier == "End") {
        return fileName + index.toString();
    }
    return index.toString()+fileName;
}

fun lowercase(fileName: String): String
{
    return fileName.lowercase()
}
fun uppercase(fileName: String): String
{
    return fileName.uppercase()
}

fun capitalize(fileName: String): String
{
    return fileName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}