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

@Composable
@Preview
fun App() {
    var folderPath by remember { mutableStateOf("Select Folder") }

    var newNameField by remember { mutableStateOf("World") }
    var checkField by remember { mutableStateOf(true) }
    var dropdownExpand by remember { mutableStateOf(false) }
    var canRename by remember { mutableStateOf(false) }
  //  var listOfFiles by remember { mutableStateOf(mutableListOf<fileListItem>()) }
    var listOfFiles by remember { mutableStateOf(listOf<fileListItem>()) }
 //   var listOfFiles: MutableList<fileListItem> by mutableStateOf(mutableListOf())

 //   var files by remember { mutableStateOf(default) }
    val chooser = JFileChooser()
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)


    MaterialTheme {
        Row {
            Column {
                Button(onClick = {
                    val returnVal = chooser.showOpenDialog(null)
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        folderPath = chooser.selectedFile.path
                        val folder = File(chooser.selectedFile.path)
                       // listOfFiles.clear()
                        for (file in folder.listFiles().asList()){
                            listOfFiles += fileListItem(file,true)
                       //     listOfFiles.add(fileListItem(file,true))
                        }
                      //  listOfFiles = folder.listFiles().asList()
                        canRename = true
                    }

                }) {
                    Text(folderPath)
                }
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "Hello World",
                    )
                    Switch(
                        onCheckedChange = {
                            checkField = !checkField
                        },
                        checked = checkField
                    )
                }
                TextField(
                    value = newNameField,
                    onValueChange = { newNameField = it },
                    label = { Text("Label") }
                )
               // modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)
                Box() {
                    IconButton(onClick = { dropdownExpand = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                    }

                    DropdownMenu(
                        expanded = dropdownExpand,
                        onDismissRequest = { dropdownExpand = false }
                    ) {
                        DropdownMenuItem(

                            onClick = { },
                            content = { Text("hi") }
                        )
                        DropdownMenuItem(
                            content = { Text("bye") },
                            onClick = { }
                        )
                    }
                }
                Button(onClick = {
                    renameFiles(listOfFiles,folderPath,newNameField)
        //            val folder = File(chooser.selectedFile.path)
        //            listOfFiles = folder.listFiles().asList()
                },
                    enabled = canRename) {
                    Text("rename")
                }
            }
      /*      LazyColumnScrollbar(
                state = listState,
                settings = ScrollbarSettings.Default
            ) */
            LazyColumn {
                items(listOfFiles) { fileItem ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Switch(
                            onCheckedChange = {
                                fileItem.check = it
                            },
                            checked = fileItem.check,

                        )
                        Text(text = " ${fileItem.file.name}")
                    }
                }

            }
        }
    }
}

fun renameFiles(listOfFiles: List<fileListItem>,folderPath: String,newName: String) {
    for ((index,fileItem) in listOfFiles.withIndex()){

        val oldFilePath = Paths.get(fileItem.file.path)
        val newFilePath = Paths.get(folderPath+"\\"+newName+index.toString()+"."+fileItem.file.extension)
        val newFile = newFilePath.toFile()

        Files.move(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING)
        //cleanup
       // newFile.delete()

    }
    JOptionPane.showMessageDialog(null, "infoMessage", "InfoBox: " + "Renamed files", JOptionPane.INFORMATION_MESSAGE);
  //  field
}

//@Stable
data class fileListItem(
    var file: File,
    var check: Boolean = false
 //   var check by remember { mutableStateOf(true) }
)
data class commandLayer(
    var command: textCommand,
    var value1: String,
    var value2: String
)



fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

