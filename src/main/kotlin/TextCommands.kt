import androidx.compose.ui.text.capitalize
import java.util.*

class TextCommands {

}

enum class TextCommand{
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

fun removeAmount(fileName: String,amount: Int?,modifier: String): String
{
    if(amount == null) return fileName

    var name = fileName
    if(modifier == "End") {
        name = fileName.removeRange(Math.max(fileName.length-amount,0),fileName.length)
    }
    else{
        name = fileName.removeRange(0,Math.min(amount,fileName.length))
    }

    return name
}

fun removeUntil(fileName: String,char: String,modifier: String): String
{
   // var indexToRemove = -1
    if(char.length == 0) return fileName
    var name = fileName
    if(modifier == "End") {
        val indexToRemove = fileName.lastIndexOf(char.first())
        if(indexToRemove != -1)
            name = fileName.removeRange(indexToRemove,fileName.length)
        else
            removeAll(fileName)
    }
    else{
        val indexToRemove = fileName.indexOf(char.first())+1
        if(indexToRemove != -1)
            name = fileName.removeRange(0,indexToRemove)
        else
            removeAll(fileName)
    }
    return name
}