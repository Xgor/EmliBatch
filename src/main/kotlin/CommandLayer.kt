import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class commandLayer(
    var command: TextCommand,
    var value1: String = "",
    var value2: String = ""
)

@Composable
fun CommandLayerList(
    list: List<commandLayer> ,
    onCloseTask: (commandLayer) -> Unit,
    programViewModel: ProgramViewModel,
    onMoveUpTask: (commandLayer) -> Unit,
    onMoveDownTask: (commandLayer) -> Unit,
//    updateNewName: (List<FileName>,SnapshotStateList<commandLayer>) -> Unit,
    modifier: Modifier = Modifier,

) {
    LazyColumn(
        modifier = modifier
    ) {
        items(list) { c ->
            CommandLayerItem(layer = c, onClose = { onCloseTask(c) },programViewModel = programViewModel,
                onMoveUp = { onMoveUpTask(c) },onMoveDown = { onMoveDownTask(c) })
        }
    }
}
/*
@Composable
fun CommandLayerItem(layer: commandLayer, onClose: () -> Unit, modifier: Modifier = Modifier) {
//    var checkedState by rememberSaveable { mutableStateOf(false) }
    var value1 by rememberSaveable { mutableStateOf("") }
    CommandLayerItem(
        commandLayer = layer,
//        checked = checkedState,
        value1 = value1,
//        onCheckedChange = { newValue -> checkedState = newValue },
        onClose = onClose,
        modifier = modifier,
    )
}
*/

@Composable
fun CommandLayerItem(
    layer: commandLayer,
    programViewModel: ProgramViewModel,
 //   checked: Boolean,
 //   value1: String,
 //   onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    var val1 by rememberSaveable { mutableStateOf(layer.value1) }
    var val2 by rememberSaveable { mutableStateOf(layer.value2) }
    var dropdownExpand by remember { mutableStateOf(false) }

    val pattern = remember { Regex("^\\d+\$") }

    Box(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .graphicsLayer {
            clip = true
            shape = RoundedCornerShape(5.dp)
        }
        .background(Color.LightGray))

        {
        Row (modifier = modifier.padding(8.dp)) {
            Column(verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())

            {
                IconButton(onClick = onMoveUp) {
                    Icon(
                        Icons.Filled.KeyboardArrowUp, contentDescription = "Close", tint = Color(0, 0, 0),
                        modifier = Modifier

                    )
                }
                IconButton(onClick = onMoveDown) {
                    Icon(
                        Icons.Filled.KeyboardArrowDown, contentDescription = "Close", tint = Color(0, 0, 0),
                        modifier = Modifier

                    )
                }
            }
            Column(modifier=Modifier.weight(7f)) {
                Text(
                    text = layer.command.toString(), fontSize = 14.sp, textAlign = TextAlign.Center,modifier = Modifier
                        .fillMaxWidth()
                )

                Row(
                     verticalAlignment = Alignment.CenterVertically
                ) {

                    when (layer.command) {
                        TextCommand.add ->{
                                TextField(
                                value = val1,
                                onValueChange ={
                                    val1 = it
                                    layer.value1 = val1
                                    programViewModel.updateNewName()
                                   // filesList = updateNewName(filesList,functionList)
                                },
                                label = { Text("Text")},
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(2.dp)
                        )
                            startStopDropdown(val2,layer,programViewModel)

                        }
                        TextCommand.enumerate ->{
                            TextField(
                                value = val1,
                                onValueChange ={

                                    if (it.isEmpty() || it.matches(pattern)) {

                                        val1 = it
                                        layer.value1 = val1
                                        programViewModel.updateNewName()
                                    }
                                },
                                label = { Text("Start at") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(2.dp)
                            )
                            startStopDropdown(val2,layer,programViewModel)
                        }
                        TextCommand.replace ->{
                            TextField(
                                value = val1,
                                onValueChange ={
                                    val1 = it
                                    layer.value1 = val1
                                    programViewModel.updateNewName()
                                },
                                label = { Text("From") },
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(2.dp)
                            )
                            TextField(
                                value = val2,
                                onValueChange = {
                                    val2 = it
                                    layer.value2 = val2
                                    programViewModel.updateNewName()
                                },
                                label = { Text("To") },
                                maxLines = 1,
                                modifier = Modifier.weight(1F)

                            )}
                        TextCommand.removeAmount -> {
                            TextField(
                                value = val1,
                                onValueChange ={

                                    if (it.isEmpty() || it.matches(pattern)) {
                                        val1 = it
                                        layer.value1 = val1
                                        programViewModel.updateNewName()
                                    }
                                },
                                label = { Text("Remove amount") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(2.dp)
                            )
                            startStopDropdown(val2,layer,programViewModel)
                        }

                        TextCommand.removeUntil -> {
                            TextField(
                                value = val1,
                                onValueChange ={

                                    if (it.isEmpty() || it.count() == 1) {
                                        val1 = it
                                        layer.value1 = val1
                                        programViewModel.updateNewName()
                                    }
                                },
                                label = { Text("Until") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(2.dp)
                            )
                            startStopDropdown(val2,layer,programViewModel)
                        }
                        else ->
                            Text(text = "",
                                modifier = Modifier.weight(1F))
                    }


                }
            }
            Column(verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight())

            {
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Filled.Delete, contentDescription = "Close", tint = Color(177, 0, 0),
                        modifier = Modifier

                    )
                }
            }
        }
    }
}

@Composable
fun startStopDropdown(value: String,layer: commandLayer,programViewModel: ProgramViewModel){
    var dropdownExpand by remember { mutableStateOf(false) }
    var _value by remember { mutableStateOf(value) }
    Box() {
        TextButton(onClick = { dropdownExpand = true }) {
            Text(_value+" v")
        }
        DropdownMenu(
            expanded = dropdownExpand,
            onDismissRequest = { dropdownExpand = false }
        ) {
            DropdownMenuItem(
                content = { Text("Start") },
                onClick = {
                    dropdownExpand = false
                    _value = "Start"
                    layer.value2 = _value
                    programViewModel.updateNewName()
                }
            )
            DropdownMenuItem(
                content = { Text("End") },
                onClick = {
                    dropdownExpand = false
                    _value = "End"
                    layer.value2 = _value
                    programViewModel.updateNewName()
                }
            )
        }
    }
}
