package com.yunusemre

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class HDFilmCehennemiProvider : MainAPI() { 
    override var mainUrl = "https://www.hdfilmcehennemi.nl"
    override var name = "HDFilmCehennemi"
    override val hasMainPage = true
    override var lang = "tr"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun getMainPage(page: Int, request: HomePageRequest): HomePageResponse {
        // Burası sitenin ana sayfasındaki filmleri çekmek içindir
        val document = app.get(mainUrl).document
        val home = document.select("div.poster").mapNotNull {
            it.toSearchResult()
        }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val title = this.selectFirst("h2")?.text() ?: return null
        val href = fixUrl(this.selectFirst("a")?.attr("href") ?: return null)
        val posterUrl = fixUrl(this.selectFirst("img")?.attr("data-src") ?: "")

        return newMovieSearchResponse(title, href, TvType.Movie) {
            this.posterUrl = posterUrl
        }
    }
}
