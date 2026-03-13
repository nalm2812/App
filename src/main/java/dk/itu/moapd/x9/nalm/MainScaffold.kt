/*
 * MIT License
 *
 * Copyright (c) 2026 Fabricio Batista Narcizo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dk.itu.moapd.x9.nalm

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy

import com.google.firebase.auth.FirebaseAuth


/**
 * This class represents a top-level destination in the application.
 *
 * @property route The route for the destination.
 * @property labelRes The resource ID for the destination's label.
 * @property iconRes The resource ID for the destination's icon.
 */
private data class TopLevelDestination(
    val route: String,
    val labelRes: Int,
    val iconRes: Int
)

/**
 * A list of top-level destinations in the application.
 *
 * Each destination has a route, a label resource ID, and an icon resource ID.
 *
 * @see TopLevelDestination
 */
private val destinations = listOf(
    TopLevelDestination("articles", R.string.screen_articles, R.drawable.baseline_article_24),
    TopLevelDestination("contacts", R.string.screen_contacts, R.drawable.baseline_people_24),
    TopLevelDestination("albums", R.string.screen_albums, R.drawable.baseline_dashboard_24),
)

/**
 * The main scaffold for the application.
 *
 * @param auth The Firebase authentication instance.
 * @param onLogout The callback to be invoked when the user logs out.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    auth: FirebaseAuth,
    onLogout: () -> Unit,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var showUserInfo by rememberSaveable { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    if (showUserInfo) {
        UserInfoDialog(
            auth = auth,
            onDismiss = { showUserInfo = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Title changes with destination label.
                    val titleRes = when (currentDestination?.route) {
                        "contacts" -> R.string.screen_contacts
                        "albums" -> R.string.screen_albums
                        else -> R.string.screen_articles
                    }
                    Text(text = stringResource(id = titleRes))
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.user_info_title)) },
                                onClick = {
                                    showMenu = false
                                    showUserInfo = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.action_logout)) },
                                onClick = {
                                    showMenu = false
                                    onLogout()
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (!isLandscape) {
                NavigationBar {
                    destinations.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(destination.iconRes),
                                    contentDescription = null
                                )
                            },
                            label = { Text(stringResource(destination.labelRes)) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            if (isLandscape) {
                NavigationRail {
                    destinations.forEach { destination ->
                        val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
                        NavigationRailItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(destination.iconRes),
                                    contentDescription = null
                                )
                            },
                            label = { Text(stringResource(destination.labelRes)) }
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navController,
                    startDestination = destinations.first().route
                ) {
                    composable("articles") { ArticlesScreen() }
                    composable("contacts") { ContactsScreen() }
                    composable("albums") { AlbumsScreen() }
                }
            }
        }
    }
}
