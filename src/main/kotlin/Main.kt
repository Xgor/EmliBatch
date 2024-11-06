import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import TextCommands
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.unit.dp

private fun getCommandList() = List(0) { i -> commandLayer(textCommand.removeAll, "","") }

@Composable
@Preview
fun App() {
    var folderPath by remember { mutableStateOf("Select Folder") }

    var newNameField by remember { mutableStateOf("World") }
  //  var checkField by remember { mutableStateOf(true) }
    var dropdownExpand by remember { mutableStateOf(false) }
    var canRename by remember { mutableStateOf(false) }
  //  var listOfFiles by remember { mutableStateOf(mutableListOf<fileListItem>()) }
    var listOfFiles by remember { mutableStateOf(listOf<fileListItem>()) }

    var filesList by remember { mutableStateOf(listOf<FileName>()) }

    var functionList = remember { getCommandList().toMutableStateList() }

    val chooser = JFileChooser()
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)

    var filesListState = remember {
        listOfFiles.toMutableStateList()
    }

    MaterialTheme {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(modifier = Modifier.weight(1F)) {
                Button(onClick = {
                    val returnVal = chooser.showOpenDialog(null)
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        folderPath = chooser.selectedFile.path
                        val folder = File(chooser.selectedFile.path)
                       // listOfFiles.clear()
                        var fileList = mutableListOf<FileName>()
                        for (file in folder.listFiles().asList()){
                            listOfFiles += fileListItem(file,true)
                            fileList.add(FileName(0,file.name,""))
                        }

                        filesList = fileList.toMutableStateList()
                        filesListState = listOfFiles.toMutableStateList()
                        canRename = true
                    }

                }) {
                    Text(folderPath)
                }

               // modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)
                CommandLayerList(functionList,onCloseTask = { task -> functionList.remove(task) })
                Box() {
                    IconButton(onClick = { dropdownExpand = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Localized description")
                    }

                    DropdownMenu(
                        expanded = dropdownExpand,
                        onDismissRequest = { dropdownExpand = false }
                    ) {
                        DropdownMenuItem(
                            content = { Text("add") },
                            onClick = {
                                functionList += commandLayer(textCommand.add,"","")
                                dropdownExpand = false
                            }
                        )
                        DropdownMenuItem(

                            content = { Text("removeAll") },
                            onClick = {
                                functionList += commandLayer(textCommand.removeAll,"","")
                                dropdownExpand = false
                            }
                        )
                        DropdownMenuItem(

                            content = { Text("enumerate") },
                            onClick = {
                                functionList += commandLayer(textCommand.enumerate,"","")
                                dropdownExpand = false
                            }
                        )
                        DropdownMenuItem(
                            content = { Text("replace") },
                            onClick = {
                                functionList += commandLayer(textCommand.replace,"","")
                                dropdownExpand = false
                            }
                        )
                        DropdownMenuItem(
                            content = { Text("lowercase") },
                            onClick = {
                                functionList += commandLayer(textCommand.lowercase,"","")
                                dropdownExpand = false
                            }
                        )
                        DropdownMenuItem(
                            content = { Text("uppercase") },
                            onClick = {
                                functionList += commandLayer(textCommand.uppercase,"","")
                                dropdownExpand = false
                            }
                        )
                        DropdownMenuItem(

                            content = { Text("capitalize") },
                            onClick = {
                                functionList += commandLayer(textCommand.capitalize,"","")
                                dropdownExpand = false
                            }
                        )
                        DropdownMenuItem(

                            content = { Text("removeUntil") },
                            onClick = { }
                        )
                        DropdownMenuItem(
                            content = { Text("removeAmount") },
                            onClick = { }
                        )
                    }
                }


                /*
                LazyColumn() {
                    items(functionList) { function ->
                        Text(
                            text = function.command.name,
                        )
                    }
                }
                */
                Button(onClick = {
                    renameFiles(listOfFiles,folderPath,functionList)
                },
                    enabled = canRename) {
                    Text("rename")
                }
            }
            FileNamesList(filesList,modifier = Modifier.weight(1F))

        }
    }
}

//@Stable
data class fileListItem(
    var file: File,
    var checked: Boolean = false
    //   var check by remember { mutableStateOf(true) }
)

@Composable
fun FileNamesList(
    list: List<FileName> = remember { getFileNames() },
    modifier: Modifier = Modifier

) {
    LazyColumn(
        modifier = modifier
    ) {
        items(list) { task ->
            FileNameItem(taskName = task.label)
        }
    }
}

fun getFileNames() = List(30) { i -> FileName(i, "Task # $i","") }

data class FileName(val id: Int, val label: String,var newName: String)



@Composable
fun FileNameItem(taskName: String, modifier: Modifier = Modifier) {
    var checkedState by rememberSaveable { mutableStateOf(true) }

    FileNameItem(
        taskName = taskName,
        checked = checkedState,
        onCheckedChange = { newValue -> checkedState = newValue },
        onClose = {}, // we will implement this later!
        modifier = modifier,
    )
}


@Composable
fun FileNameItem(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .size(width = 100.dp, 20.dp)
                .padding(start = 16.dp)
                .weight(1f)
            ,
            text = taskName
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        /*
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }*/
    }
}




fun renameFiles(listOfFiles: List<fileListItem>,folderPath: String,commandList: List<commandLayer>) {
    for ((index,fileItem) in listOfFiles.withIndex()){
        /*
        val commandsTest = listOf(commandLayer(textCommand.removeAll,"",""),
            commandLayer(textCommand.add,newName,"Start"),
            commandLayer(textCommand.enumerate,"End","End"))
        */
        val oldFilePath = Paths.get(fileItem.file.path)
        val newFileName = generateFilename(fileItem.file.name,commandList,index)
        val newFilePath = Paths.get("${folderPath}\\${newFileName}.${fileItem.file.extension}")
        val newFile = newFilePath.toFile()

        Files.move(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING)
        //cleanup
       // newFile.delete()

    }
    JOptionPane.showMessageDialog(null, "infoMessage", "InfoBox: " + "Renamed files", JOptionPane.INFORMATION_MESSAGE);
  //  field
}

fun generateFilename(oldName: String,commands: List<commandLayer>,index: Int) : String
{
    var name = oldName
    for (c in commands)
        name = when (c.command) {
            textCommand.removeAll -> removeAll(name)
            textCommand.add -> add(name,c.value1,c.value2)
            textCommand.replace -> replace(name,c.value1,c.value2)
            textCommand.enumerate -> enumerate(name,index,c.value2)
            textCommand.lowercase -> lowercase(name)
            textCommand.uppercase -> uppercase(name)
            textCommand.removeAmount -> removeAmount(name,c.value1.toInt(),c.value2)
            textCommand.capitalize -> capitalize(name,c.value1)
            textCommand.removeUntil -> removeUntil(name,c.value1.toInt(),c.value2)
        }
    return name
}





fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

