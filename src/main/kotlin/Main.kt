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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private fun getCommandList() = List(0) { i -> commandLayer(TextCommand.removeAll, "","") }

/*
class Test{
    companion object{
        var functionList = remember { getCommandList().toMutableStateList() }
        var filesList by remember { mutableStateOf(listOf<FileName>()) }
    }
}
*/
@Composable
@Preview
fun AppPreview()
{
    MaterialTheme {
        App()
    }
}

@Composable
//@Preview
fun App(programViewModel: ProgramViewModel = viewModel()) {
    var folderPath by remember { mutableStateOf("Select Folder") }

    var newNameField by remember { mutableStateOf("World") }
  //  var checkField by remember { mutableStateOf(true) }
    var dropdownExpand by remember { mutableStateOf(false) }
    var canRename by remember { mutableStateOf(false) }
  //  var listOfFiles by remember { mutableStateOf(mutableListOf<fileListItem>()) }

    var listOfFiles by remember { mutableStateOf(listOf<fileListItem>()) }

  //  var filesList by remember { programViewModel.GetFilesList() }
    var filesList by remember { mutableStateOf(listOf<FileName>()) }
    var functionList = remember { getCommandList().toMutableStateList() }

    val chooser = JFileChooser()
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
/*
    var filesListState = remember {
        listOfFiles.toMutableStateList()
    }*/
    val gameUiState by programViewModel.uiState.collectAsState()
 //   MaterialTheme {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(modifier = Modifier.weight(1F).fillMaxWidth(), verticalArrangement = Arrangement.Center) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = {
                    programViewModel.selectFolderPath()
                }) {
                    Text(gameUiState.folderPath)
                }
            }

           // modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)
            CommandLayerList(gameUiState.commandList,onCloseTask = { task -> programViewModel.RemoveCommand(task) },
                onMoveUpTask = { task -> programViewModel.MoveCommand(task,-1) },
                onMoveDownTask = { task -> programViewModel.MoveCommand(task,1) },programViewModel=programViewModel)
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
                            dropdownExpand = false
                            programViewModel.AddCommand(TextCommand.add)

   //                         functionList += commandLayer(TextCommand.add,"","")

 //                           filesList = updateNewName(filesList,functionList)
                        }
                    )
                    DropdownMenuItem(

                        content = { Text("removeAll") },
                        onClick = {
                            programViewModel.AddCommand(TextCommand.removeAll)
//                            functionList += commandLayer(TextCommand.removeAll,"","")
                            dropdownExpand = false
 //                           filesList = updateNewName(filesList,functionList)

                        }
                    )
                    DropdownMenuItem(

                        content = { Text("enumerate") },
                        onClick = {
                            dropdownExpand = false
                            programViewModel.AddCommand(TextCommand.enumerate)

                        }
                    )
                    DropdownMenuItem(
                        content = { Text("replace") },
                        onClick = {
                            dropdownExpand = false
                            programViewModel.AddCommand(TextCommand.replace)
                        }
                    )
                    DropdownMenuItem(
                        content = { Text("lowercase") },
                        onClick = {
                            dropdownExpand = false
                            programViewModel.AddCommand(TextCommand.lowercase)
                        }
                    )
                    DropdownMenuItem(
                        content = { Text("uppercase") },
                        onClick = {
                            dropdownExpand = false
                            programViewModel.AddCommand(TextCommand.uppercase)
                        }
                    )
                    DropdownMenuItem(

                        content = { Text("capitalize") },
                        onClick = {
                            dropdownExpand = false
                            programViewModel.AddCommand(TextCommand.capitalize)
                        }
                    )

                    DropdownMenuItem(

                        content = { Text("removeUntil") },
                        onClick = {
                            dropdownExpand = false
                            programViewModel.AddCommand(TextCommand.removeUntil)
                        }
                    )
                    DropdownMenuItem(
                        content = { Text("removeAmount") },
                        onClick = {
                            dropdownExpand = false
                            programViewModel.AddCommand(TextCommand.removeAmount)
                        }
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
                programViewModel.renameFiles()
              //  renameFiles(listOfFiles,folderPath,functionList)
            },
                enabled = gameUiState.canRename) {
                Text("rename")
            }
        }
        FileNamesList(gameUiState.fileList,modifier = Modifier.weight(1F), viewModel = programViewModel)

        }
 //   }
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
    modifier: Modifier = Modifier,viewModel: ProgramViewModel

) {
    LazyColumn(
        modifier = modifier
    ) {
        items(list) { task ->
            FileNameItem(task = task,viewModel= viewModel)
        }
    }
}

fun getFileNames() = List(30) { i -> FileName(i, "Task # $i","","") }

data class FileName(val id: Int, val oldName: String,var newName: String,var extension: String,var enabled: Boolean = true)



@Composable
fun FileNameItem(task: FileName, modifier: Modifier = Modifier,viewModel: ProgramViewModel) {
    var checkedState by rememberSaveable { mutableStateOf(task.enabled) }

    FileNameItem(
        task = task,
        checked = checkedState,
        onCheckedChange = { newValue ->
            checkedState = newValue
            viewModel.changeCheckedFile(task,newValue)
                          },
        modifier = modifier,
//       viewModel = viewModel
    )
}


@Composable
fun FileNameItem(
    task: FileName,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
//    viewModel: ProgramViewModel
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
            text = task.oldName + "." + task.extension
        )
        if (checked) {
            Text(
                modifier = Modifier
                    .size(width = 100.dp, 20.dp)
                    .padding(start = 16.dp)
                    .weight(1f),
                text = task.newName + "." + task.extension,
                color = Color.Blue
            )
        }
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





fun main() = application {
//    val icon = painterResource("icon.ico")

    val state = rememberWindowState(
        width = 1280.dp,
        height = 720.dp,
    )
    Window(onCloseRequest = ::exitApplication,
        title = "Emli Batch",
        state = state) {
        App()
    }
}

