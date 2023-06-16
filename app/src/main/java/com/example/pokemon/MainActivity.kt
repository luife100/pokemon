package com.example.pokemon

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pokemon.ui.theme.PokemonTheme
import com.example.pokemon.viewModel.HomeViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val  vm = ViewModelProvider(this)[HomeViewModel::class.java]
        setContent {
            PokemonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisplayPokemons(vm)
                }
            }
        }
    }

    @Composable
    private fun DisplayPokemons(vm: HomeViewModel, modifier: Modifier = Modifier) {
        val scrollState = rememberLazyListState()
        val pokemonsList = vm.postModelListLiveData.observeAsState()
        var showDialog by rememberSaveable { mutableStateOf(false)  }
        if (showDialog) ShowDialog(vm, onDismiss ={ showDialog = false}, modifier = Modifier)
        Column {
            LazyColumn(state=scrollState) {
                itemsIndexed(pokemonsList.value ?: listOf()) { index, item ->
                    Text(text = item.name,
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .clickable {
                                vm.getPokemon(index + 1)
                                showDialog = true
                            })
                }
            }
            val endOfListReached by remember {
                derivedStateOf {
                    scrollState.isScrolledToEnd()
                }
            }

            // act when end of list reached
            LaunchedEffect(endOfListReached) {
                vm.increaseList()
            }
        }
}

    @Composable
    private fun ShowDialog(vm: HomeViewModel, modifier: Modifier, onDismiss: () -> Unit) {
        val pokemon = vm.pokemonLiveData.observeAsState()
        Dialog(onDismissRequest = onDismiss) {
                Surface(modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)) {

            pokemon.value?.let {
                Column {
                    Text("Name: ${it.name}")
                    Text("Height: ${it.height}")
                    Text("Order Number: ${it.id}")
                    Text("Weight: ${it.weight}")
                    Button(onClick =  onDismiss) {
                        Text(text = "Close")
                    }
                }

            }

        }}
    }


    @Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokemonTheme {
        DisplayPokemons(HomeViewModel())
    }
}}

fun LazyListState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1