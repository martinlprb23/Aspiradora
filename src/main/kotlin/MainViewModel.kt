import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*

class MainViewModel {

    var count = mutableStateOf(0)                               //Contador de basura
    var isLoading = mutableStateOf(false)                       //Estado del recorrido
    val list = mutableStateListOf<Int>()                              //Lista mutable
    private var data by mutableStateOf(Array(64) { index -> 0 }) //Arrreglo de 64

    //Funcion init que incia automaticamente al instanciar la clase
    init {
        //ForEach para llenar la lista mediante el Array
        data.forEach {
            list.add(it)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun clean() {
        GlobalScope.launch {
            delay(1000L)
            println("Inciando limpieza!")

            val startIndex = list.indexOf(2)
            if (startIndex >= 0) {
                var position = startIndex
                list[startIndex] = 0
                // Definir las direcciones posibles (arriba, abajo, izquierda, derecha)
                val directions = listOf(-8, 8, -1, 1)
                var dirtyRemaining = list.count { it == 1 } // Basura sobrante
                while (dirtyRemaining > 0) {
                    val random = (0..3).random() // Posibles direcciones
                    val direction = directions[random]
                    val newPosition = position + direction

                    // Verificar si la posición actual y la nueva posición están en lados opuestos
                    val isOppositeSide = (position % 8 == 0 && newPosition % 8 == 7) ||
                            (position % 8 == 7 && newPosition % 8 == 0) ||
                            (position % 8 == 0 && newPosition % 8 == 7)


                    // Verificar si la nueva posición está dentro de los límites del grid
                    if (newPosition >= 0 && newPosition < list.size && !isOppositeSide) {
                        if (list[newPosition] == 1) {
                            println("Limpiando basura en el item: $newPosition\n")
                            dirtyRemaining--
                        }
                        // Determinar la dirección en función del movimiento
                        val moveDirection = when (direction) {
                            -8 -> "arriba"
                            8 -> "abajo"
                            -1 -> "izquierda"
                            1 -> "derecha"
                            else -> "desconocida"
                        }
                        println("Aspiradora en el item: $newPosition --- Dirección: $moveDirection")
                        list[newPosition] = 2      // Cambiar el estado de sucio y colocar la aspiradora
                        withContext(Dispatchers.IO) {
                            Thread.sleep(200L)
                        }  // Delay
                        list[newPosition] = 0  // Cambiar el estado a limpio después de pasar la aspiradora
                        position = newPosition
                    }

                }

                println("Limpieza finalizada... Son 5 varos + 10 de luz")
                count.value = 0
                isLoading.value = false
            }
        }
    }

}


