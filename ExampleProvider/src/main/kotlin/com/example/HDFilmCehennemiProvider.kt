class HDFilmCehennemiProvider : MainAPI() { 
    override var mainUrl = "https://www.hdfilmcehennemi.nl"
    override var name = "HDFilmCehennemi"
    override val hasMainPage = true
    override var lang = "tr"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    // Buraya arama, ana sayfa ve video linklerini çekme fonksiyonları gelecek
}
