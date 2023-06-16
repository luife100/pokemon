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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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

        //vm.getPokemonsList()}

        vm.postModelListLiveData?.observe(this, Observer {
            println("ViewModel changed!")
            if (it!=null){
                println("This is the list: $it")
            }else{
                Toast.makeText(this,"Something Wrong",Toast.LENGTH_LONG).show()
                println("No list found: $it")
            }

        })
        vm.pokemonLiveData.observe(this, Observer {
            println("Pokemon changed!")
            if (it!=null){
                println("This is the pokemon: $it")
            }else{
                Toast.makeText(this,"Something Wrong Pokemon",Toast.LENGTH_LONG).show()
                println("No pokemon found: $it")
            }

        })

    }

    @Composable
    private fun DisplayPokemons(vm: HomeViewModel, modifier: Modifier = Modifier) {
        val pokemonsList = vm.postModelListLiveData.observeAsState()
        var showDialog by rememberSaveable { mutableStateOf(false)  }
        var offset by rememberSaveable { mutableStateOf(vm.offset)  }
        if (showDialog) ShowDialog(vm, onDismiss ={ showDialog = false}, modifier = Modifier)
        Column {
          Row{
              Button(onClick = {
                  offset = vm.decreaseOffset()
                               }, enabled = offset!=null) {
                  Text(text = "Previous")
              }
              Button(onClick = {
                  offset= vm.increaseOffset()
              }) {
                Text(text = "Next")
            }
            }
            LazyColumn {
                itemsIndexed(pokemonsList.value ?: listOf()) { index, item ->
                    Text(text = item.name,
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .clickable {
                                val offsetNotNull:Int = offset ?: 0
                                vm.getPokemon(index + 1+offsetNotNull)
                                showDialog = true
                            })
                }
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