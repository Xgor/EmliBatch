import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class commandLayer(
    var command: textCommand,
    var value1: String,
    var value2: String
)

@Composable
fun CommandLayerList(
    list: List<commandLayer> ,
    onCloseTask: (commandLayer) -> Unit,
    modifier: Modifier = Modifier

) {
    LazyColumn(
        modifier = modifier
    ) {
        items(list) { c ->
            CommandLayerItem(layer = c, onClose = { onCloseTask(c) })
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
 //   checked: Boolean,
 //   value1: String,
 //   onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var val1 by rememberSaveable { mutableStateOf("") }
    var val2 by rememberSaveable { mutableStateOf("") }
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
            Column(modifier=Modifier.weight(7f)) {
                Text(
     //               modifier = Modifier
     //                   .padding(start = 8.dp)
     //                   .weight(1f),
                    text = layer.command.toString(), fontSize = 14.sp, textAlign = TextAlign.Center,modifier = Modifier
                        .fillMaxWidth()
                )

                Row(
                     verticalAlignment = Alignment.CenterVertically
                ) {

                /*
                        Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange
                )*/
                    when (layer.command) {
                        textCommand.add ->{
                                TextField(
                                value = val1,
                                onValueChange ={
                                    val1 = it
                                    layer.value1 = val1
                                },
              //              onValueChange = { value1 = it },
                                label = { Text("Text")},
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(2.dp)
                        )}
                        textCommand.replace ->{
                            TextField(
                                value = val1,
                                onValueChange ={
                                    val1 = it
                                    layer.value1 = val1
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
                                },
                                label = { Text("To") },
                                maxLines = 1,
                                modifier = Modifier.weight(1F)

                            )}
            //            textCommand.enumerate -> TODO()
                        textCommand.removeAmount -> TODO()
            /*            textCommand.lowercase -> TODO()
                        textCommand.uppercase -> TODO()
                        textCommand.capitalize -> TODO()*/
                        textCommand.removeUntil -> TODO()
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

