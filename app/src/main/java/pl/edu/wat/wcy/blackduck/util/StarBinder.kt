package pl.edu.wat.wcy.blackduck.util

import android.widget.ImageButton
import pl.edu.wat.wcy.blackduck.R
import pl.edu.wat.wcy.blackduck.data.responses.RateResponse

class StarBinder {

    companion object {
        fun bindStars(
            rates: List<RateResponse>,
            rate: Int?,
            displayName: String,
            stars: List<ImageButton>
        ) {
            var userRate: Int? =
                rates.firstOrNull { it.fromUser.username == displayName }
                    ?.rate
            if (userRate == null) userRate = 0
            if (rate != null) userRate = rate

            when (userRate) {
                5 -> {
                    stars[0].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[1].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[2].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[3].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[4].setImageResource(R.drawable.ic_star_fill_24dp)
                }
                4 -> {
                    stars[0].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[1].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[2].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[3].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[4].setImageResource(R.drawable.ic_star_border_24dp)
                }
                3 -> {
                    stars[0].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[1].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[2].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[3].setImageResource(R.drawable.ic_star_border_24dp)
                    stars[4].setImageResource(R.drawable.ic_star_border_24dp)
                }
                2 -> {
                    stars[0].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[1].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[2].setImageResource(R.drawable.ic_star_border_24dp)
                    stars[3].setImageResource(R.drawable.ic_star_border_24dp)
                    stars[4].setImageResource(R.drawable.ic_star_border_24dp)
                }
                1 -> {
                    stars[0].setImageResource(R.drawable.ic_star_fill_24dp)
                    stars[1].setImageResource(R.drawable.ic_star_border_24dp)
                    stars[2].setImageResource(R.drawable.ic_star_border_24dp)
                    stars[3].setImageResource(R.drawable.ic_star_border_24dp)
                    stars[4].setImageResource(R.drawable.ic_star_border_24dp)
                }
            }

        }
    }

}