
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.phone_book.navigation.Screen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.ContactList.route) {
        composable(route = Screen.ContactList.route) {
            ContactListScreen(title = "My Phone Book", navController = navController)
        }
    }
}