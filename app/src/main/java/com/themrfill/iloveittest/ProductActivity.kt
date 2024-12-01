package com.themrfill.iloveittest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.themrfill.iloveittest.model.Constants.PRODUCT_KEY
import com.themrfill.iloveittest.model.Product
import com.themrfill.iloveittest.model.productFromJson
import com.themrfill.iloveittest.ui.theme.ILoveItTestTheme
import com.themrfill.iloveittest.utils.tidy

class ProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val product: Product? =
            intent?.let { bundle ->
                val json = bundle.getStringExtra(PRODUCT_KEY).toString()
                productFromJson(json)
            }
        product?.let {
            setContent {
                ILoveItTestTheme {
                    Scaffold(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    ) { innerPadding ->
                        Column(modifier = Modifier.padding(dimensionResource(R.dimen.outer_margin))) {
                            TableLayout(innerPadding, product)
                        }
                    }
                }
            }
        } ?: finish()
    }
}

@Composable
fun TableLayout(innerPadding: PaddingValues, product: Product) {
    val uriHandler = LocalUriHandler.current
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        product.product_title?.let { title ->
            val newTitle = title.tidy()
            item {
                Text(
                    text = newTitle,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.inner_padding)),
                )
            }
        }
        item {
            product.product_photo?.let { photoUrl ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = photoUrl,
                        modifier = Modifier.padding(dimensionResource(R.dimen.inner_padding)),
                        contentDescription = stringResource(R.string.productImage),
                    )
                }
            }
        }
        item { TableRow(R.string.productPrice, product.product_price) }
        item { TableRow(R.string.productPriceOriginal, product.product_original_price) }
        item { TableRow(R.string.productCurrency, product.currency) }
        item { TableRow(R.string.productDelivery, product.delivery) }
        item { TableRow(R.string.productStars, product.product_star_rating)}
        product.product_url?.let { productUrl ->
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { uriHandler.openUri(productUrl) },
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            disabledContentColor = MaterialTheme.colorScheme.primary,
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.linkToProduct),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.inner_padding)),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TableRow(
    title: Int,
    text: String?,
) {
    text?.let {
        Row {
            Text(
                text = stringResource(title),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(column1Weight)
                    .padding(dimensionResource(R.dimen.inner_padding)),
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(column2Weight)
                    .padding(dimensionResource(R.dimen.inner_padding)),
            )
        }
    }
}


const val column1Weight = .3f // 30%
const val column2Weight = .7f // 70%