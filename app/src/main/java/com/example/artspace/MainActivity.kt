package com.example.artspace

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.artspace.data.Artwork
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtSpaceTheme {
                ArtView()
            }
        }
    }
}

@Composable
fun loadScaledImage(@DrawableRes resId: Int, maxWidth: Int, maxHeight: Int): ImageBitmap {
    val context = LocalContext.current
    return remember(resId) {
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(context.resources, resId)
            val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                val scale = minOf(
                    maxWidth.toFloat() / info.size.width,
                    maxHeight.toFloat() / info.size.height,
                    1f
                )
                val targetWidth = (info.size.width * scale).toInt()
                val targetHeight = (info.size.height * scale).toInt()
                decoder.setTargetSize(targetWidth, targetHeight)
            }
            bitmap.asImageBitmap()
        } else {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeResource(context.resources, resId, options)

            val (width, height) = options.outWidth to options.outHeight
            var inSampleSize = 1
            if (height > maxHeight || width > maxWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2
                while ((halfHeight / inSampleSize) >= maxHeight && (halfWidth / inSampleSize) >= maxWidth) {
                    inSampleSize *= 2
                }
            }

            val scaledOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
            }

            val bitmap = BitmapFactory.decodeResource(context.resources, resId, scaledOptions)
            bitmap.asImageBitmap()
        }
    }
}

@Composable
fun ArtSpace(
    modifier: Modifier = Modifier,
    artwork: Int,
    titleArt: Int,
    infoArt: Int
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(500.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, Color.LightGray, RoundedCornerShape(12.dp))
        ) {
            val image = loadScaledImage(artwork, maxWidth = 1080, maxHeight = 1080)
            Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(titleArt),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .height(120.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(infoArt),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier
                .height(120.dp)
                .padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArtView() {
    val artworks = listOf(
        Artwork(
            R.drawable.nadi_whatisdelirium_fz8uf_l52wg_unsplash,
            R.string.silver_cat,
            R.string.silver_cat_info
        ),
        Artwork(
            R.drawable.eugene_golovesov_b1pwkdbyjco_unsplash,
            R.string.bunch_of_leaves,
            R.string.bunch_of_leaves_info
        ),
        Artwork(
            R.drawable.lorin_both_egffthvqwfw_unsplash,
            R.string.orange_poppy_flower,
            R.string.orange_poppy_flower_info
        ),
        Artwork(
            R.drawable.hennie_stander_v4smlpt8i6s_unsplash,
            R.string.sun_sets_over_the_ocean,
            R.string.sun_sets_over_the_ocean_info
        ),
        Artwork(
            R.drawable.djordje_djordjevic_lptq4zznusa_unsplash,
            R.string.peaceful_beach_at_sunset,
            R.string.peaceful_beach_at_sunset_info
        ),
        Artwork(
            R.drawable.kirill_prikhodko_mgf3pku1ffi_unsplash,
            R.string.ford_mustang,
            R.string.ford_mustang_info
        ),
        Artwork(
            R.drawable.pawel_czerwinski_xa2_q_pwcou_unsplash,
            R.string.skull,
            R.string.skull_info
        ),
        Artwork(
            R.drawable.aleksandra_dementeva_prnmjrkhvpm_unsplash,
            R.string.plate_and_glasses,
            R.string.plate_and_glasses_info
        ),
        Artwork(
            R.drawable.kevin_mueller_mardxkt4gdk_unsplash,
            R.string.fog_shrouds,
            R.string.fog_shrouds_info
        ),
        Artwork(
            R.drawable.carlos_tonqdovhvj0_unsplash,
            R.string.waves_crash,
            R.string.waves_crash_info
        ),
        Artwork(
            R.drawable.aaron_burden_cmzzu6zfvto_unsplash,
            R.string.red_tulips,
            R.string.red_tulips_info
        ),
        Artwork(
            R.drawable.nastia_petruk_ibx2oiyhw2i_unsplash,
            R.string.vibrant_sunflower,
            R.string.vibrant_sunflower_info
        ),
        Artwork(
            R.drawable.laura_ufuqmun1vem_unsplash,
            R.string.inside_of_a_church,
            R.string.inside_of_a_church_info
        ),
        Artwork(
            R.drawable.hanna_lazar_nan6_wtljqu_unsplash,
            R.string.vibrant_green_plants,
            R.string.vibrant_green_plants_info
        ),
        Artwork(
            R.drawable.aaron_burden_oa6ouqfswew_unsplash,
            R.string.pretty_peach_flowers,
            R.string.pretty_peach_flowers_info
        ),
        Artwork(
            R.drawable.aaron_burden_khsl_my40ya_unsplash,
            R.string.pink_cherry_blossom,
            R.string.pink_cherry_blossom_info
        ),
        Artwork(
            R.drawable.andrew_haimerl_gvp0tnq1u0a_unsplash,
            R.string.neon_lit_alley,
            R.string.neon_lit_alley_info
        )
    )

    val currentIndex = remember { mutableStateOf(0) }

    val (artwork, title, info) = artworks[currentIndex.value]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp),
            contentAlignment = Alignment.Center
        ) {
            ArtSpace(
                artwork = artwork,
                titleArt = title,
                infoArt = info
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (currentIndex.value > 0) {
                        currentIndex.value -= 1
                    }
                },
                enabled = currentIndex.value > 0
            ) {
                Text("Previous")
            }
            Button(
                onClick = {
                    if (currentIndex.value < artworks.lastIndex) {
                        currentIndex.value += 1
                    }
                },
                enabled = currentIndex.value < artworks.lastIndex
            ) {
                Text("Next")
            }
        }
    }
}