package com.example

import org.jsoup.nodes.Element
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup

class HDFilmCehennemi : MainAPI() {
    override var mainUrl = "https://www.hdfilmcehennemi.nl"
    override var name = "HDFilmCehennemi"
    override val hasMainPage = true
    override var lang = "tr"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override val mainPage = mainPageOf(
        mainUrl to "Yeni Eklenen Filmler",
        "${mainUrl}/category/tavsiye-filmler-izle2" to "Tavsiye Filmler",
        "${mainUrl}/imdb-7-puan-uzeri-filmler" to "IMDB 7+ Filmler"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data).document
        val home = document.select("div.section-content a.poster").mapNotNull { it.toSearchResult() }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val title = this.selectFirst("strong.poster-title")?.text() ?: return null
        val href = fixUrlNull(this.attr("href")) ?: return null
        val posterUrl = fixUrlNull(this.selectFirst("img")?.attr("data-src"))
        return newMovieSearchResponse(title, href, TvType.Movie) { this.posterUrl = posterUrl }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val response = app.get("${mainUrl}/search?q=${query}", headers = mapOf("X-Requested-With" to "fetch")).parsedSafe<Results>()
        return response?.results?.map { html ->
            val doc = Jsoup.parse(html)
            newMovieSearchResponse(doc.selectFirst("h4")?.text() ?: "", fixUrl(doc.selectFirst("a")?.attr("href") ?: ""), TvType.Movie) {
                this.posterUrl = fixUrlNull(doc.selectFirst("img")?.attr("data-src"))
            }
        } ?: emptyList()
    }

    data class Results(val results: List<String> = arrayListOf())
}
