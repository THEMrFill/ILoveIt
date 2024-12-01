package com.themrfill.iloveittest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.themrfill.iloveittest.model.Constants.PRODUCT_KEY
import com.themrfill.iloveittest.model.Product
import com.themrfill.iloveittest.ui.theme.ILoveItTestTheme
import com.themrfill.iloveittest.utils.tidy
import com.themrfill.iloveittest.vm.ProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val productViewModel: ProductViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ILoveItTestTheme {
                Scaffold(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(dimensionResource(R.dimen.outer_margin)),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val keyboard = LocalSoftwareKeyboardController.current
                            OutlinedTextField(
                                value = productViewModel.searchString.value,
                                onValueChange = { productViewModel.searchString.value = it },
                                label = { Text(stringResource(R.string.search)) },
                                modifier = Modifier.padding(innerPadding),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.primary,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                ),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = { productViewModel.searchProduct(keyboard) }
                                ),
                            )
                            Spacer(
                                modifier = Modifier.width(dimensionResource(R.dimen.outer_margin_small))
                            )

                            val container = MaterialTheme.colorScheme.primaryContainer
                            val radius = dimensionResource(R.dimen.circle_size)
                            IconButton(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .drawWithCache {
                                        onDrawBehind {
                                            drawCircle(color = container, radius = radius.toPx())
                                        }
                                    },
                                onClick = {
                                    productViewModel.searchProduct(keyboard)
                                },
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_search_24),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(dimensionResource(R.dimen.inner_padding)),
                                    contentDescription = stringResource(R.string.search_now),
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            WaitSpinner(productViewModel.showSpinner)
                            ErrorMessage(productViewModel.showError)
                            ProductList()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductList() {
    val productViewModel: ProductViewModel = viewModel()
    val productList = productViewModel.productList
    val productCount = productViewModel.productCount.intValue
    val pageList = productViewModel.getPageList()

    LazyColumn {
        if (productList.size > 0) {
            item {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.search_results),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(dimensionResource(R.dimen.inner_padding)),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            var i: Int = 0
            items(productList.size) { product ->
                ProductCard(productList[product], i++)
            }
            if (productCount > productList.size) {
                item {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        PagerButton(pageList, 0)
                        PagerButton(pageList, 1)
                        Text(
                            text = stringResource(
                                R.string.page,
                                productViewModel.productPage.intValue
                            )
                        )
                        PagerButton(pageList, 2)
                        PagerButton(pageList, 3)
                    }
                }
            }
        }
    }
}

@Composable
fun PagerButton(pageList: MutableList<Int?>, pageNumber: Int) {
    val productViewModel: ProductViewModel = viewModel()
    IconButton(
        onClick = {
            pageList[pageNumber]?.let { page ->
                productViewModel.findProducts(productViewModel.searchString.value, page)
            }
        }
    ) {
        Icon(
            painter = painterResource(
                when (pageNumber) {
                    0 -> R.drawable.baseline_keyboard_double_arrow_left_24
                    1 -> R.drawable.baseline_keyboard_arrow_left_24
                    2 -> R.drawable.baseline_keyboard_arrow_right_24
                    3 -> R.drawable.baseline_keyboard_double_arrow_right_24
                    else -> R.drawable.baseline_search_24
                }
            ),
            tint = if (pageList[pageNumber] == null) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.secondary
            },
            modifier = Modifier.padding(dimensionResource(R.dimen.outer_margin_small)),
            contentDescription = stringResource(
                when (pageNumber) {
                    0 -> R.string.first_page
                    1 -> R.string.previous_page
                    2 -> R.string.next_page
                    3 -> R.string.last_page
                    else -> R.string.search
                }
            ),
        )
    }
}

@Composable
fun ProductCard(product: Product, i: Int) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.inner_padding))
            .testTag("Row $i"),
    ) {
        Button(
            onClick = { showProduct(product, context) },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Column {
                val title = product.product_title?.tidy()
                    ?: stringResource(R.string.unknownTitle)
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                product.product_price?.let { price ->
                    Text(
                        text = price,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

fun showProduct(data: Product, context: Context) {
    val intent = Intent(context, ProductActivity::class.java)
    intent.putExtra(PRODUCT_KEY, data.toJson())
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ILoveItTestTheme {
        Text(stringResource(R.string.app_name))
    }
}

@Composable
fun WaitSpinner(spinnerVisible: MutableTransitionState<Boolean>) {
    AnimatedVisibility(visibleState = spinnerVisible) {
        CircularProgressIndicator(
            modifier = Modifier.width(dimensionResource(R.dimen.spinner_size)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.background,
        )
    }
}

@Composable
fun ErrorMessage(textVisible: MutableTransitionState<Boolean>) {
    AnimatedVisibility(visibleState = textVisible) {
        Text(
            text = stringResource(R.string.load_error),
            modifier = Modifier.padding(dimensionResource(R.dimen.inner_padding)),
        )
    }
}