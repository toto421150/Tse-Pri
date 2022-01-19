package com.example.pri_LockBand

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.speedchecker.android.sdk.Public.SpeedTestListener
import com.speedchecker.android.sdk.Public.SpeedTestResult
import com.speedchecker.android.sdk.SpeedcheckerSDK
import java.util.*

class MainActivity : AppCompatActivity(), SpeedTestListener {

    private lateinit var itemAdapter: ItemAdapter //Affichage de la liste
    var listInfos = mutableListOf<String>() // Store les informations pour chaque antenne

    //XML : elements du speed test
    private lateinit var mTextViewStage: TextView
    private lateinit var mTextViewResult: TextView
    private lateinit var mButtonSpeedTest: Button

    //Function to find & actualize the antenna information
    @SuppressLint("MissingPermission")
    fun initData() {
        //Get all data with TelephonyManager
        var telMng = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        //Store it into a String
        val telMngList = telMng.allCellInfo.toString()
        //Define values to parse
        val cellParsing = telMngList.split("CellInfo") //Decoupe les données brutes pour chaque antenne
        //Parse data
        listInfos = parsingData(cellParsing,telMngList)
        //Refresh the data by removing old values then by adding new values
        itemAdapter.deleteOldVals()
        listInfos.forEach {
            val antennaItem = it

            if (antennaItem.isNotEmpty()) {
                val item = AntennaItem(antennaItem)
                itemAdapter.addNewVals(item)
            }
        }
    }

    /*fun debugList(cellParsing: List<String>):String{ //Fonction de debug affichant dans une textview l'ensemble des données non parsées afin de vérifier les informations
        var debugText=""
        for (i in cellParsing.indices) {
            if (i!=0){
                debugText = debugText + (cellParsing[i]) + "\n\n"
            }
        }

        return debugText
    }*/

    fun parsingData(cellParsing: List<String>, telMngList: String): MutableList<String>{
        var parse:Boolean=false //Eviter le parsing en cas de valeurs nulles (pour ne pas crash)
        var networkType:String="" //Permet de get le network type
        val listInfos = mutableListOf<String>()
        var pcis:List<String> = listOf()
        var earfcn:List<String> = listOf()
        var dBms:List<String> = listOf()
        var bandWidth:List<String> = listOf()

        for (i in cellParsing.indices) { // Loop pour chaque antenne
            parse=false

            if (cellParsing[i].startsWith("Nr")){ //Si antenne type 5G
                networkType="Nr"
                pcis = cellParsing[i].split("mPci = ")
                earfcn = cellParsing[i].split("mNrArfcn = ")
                dBms = cellParsing[i].split("ssRsrp = ")
                bandWidth = cellParsing[i].split("csiRsrp = ")
                parse=true
            }
            if (cellParsing[i].startsWith("Lte")){ //Si antenne type 4G
                networkType="Lte"
                pcis = cellParsing[i].split("mPci=")
                earfcn = cellParsing[i].split("mEarfcn=")
                dBms = cellParsing[i].split("rsrp=")
                bandWidth = cellParsing[i].split("mBandwidth=")
                parse=true
            }
            if (cellParsing[i].startsWith("Wcdma")){ //Si antenne type 3G
                networkType="Wcdma"
                pcis = cellParsing[i].split("mPsc=")
                earfcn = cellParsing[i].split("mUarfcn=")
                dBms = cellParsing[i].split("rscp=")
                bandWidth = listOf()
                parse=true
            }

            if (i!=0 && parse && dBms[1].split(' ')[0].toInt()<0){ //Ajout a la liste des élements apres la premiere itération (vide) et suppression de l'antenne du telephone (valeur abérantes)

                listInfos.add(" PCi = " + pcis[1].split(' ')[0] + "\n"+ " EARFCN = " + earfcn[1].split(' ')[0] + "\n " + searchBand(earfcn[1].split(' ')[0].toInt(),networkType)+getBandwidth(bandWidth)+ " SignalStrength = " + dBms[1].split(' ')[0] + " dBm\n")
            }
        }
    return listInfos
    }

    fun getBandwidth(bandWidth: List<String>):String{ //Permet de retourner l'information de la bandwidth (si elle existe dans le parsing) mise en forme
        if (bandWidth.isEmpty()){
            return ""
        }else{
           return( " Bandwidth = " + bandWidth[1].split(' ')[0].toFloat()/1000000 + " MHz\n")
        }

    }

    fun searchBand(int: Int,networkType:String): String { ///Associe les bandes avec leur EARFCN et renvoie le nom de la bande
        var band: String = "" //Store la bande associée au EARFCN
        if (networkType == "Lte"){
            when (int) {

                //4G LTE most used bands -----------------
                in 1200..1949 -> { //Type 4G

                    band = "Band = B3 (1800MHz) 4G FDD LTE\n"
                }
                in 2750..3449 -> {

                    band = "Band = B7 (2600MHz) 4G FDD LTE\n"
                }
                in 6150..6449 -> {

                    band = "Band = B20 (800MHz) 4G FDD LTE\n"
                }
                in 0..599 -> {

                    band = "Band = B1 (2100MHz) 4G FDD LTE\n"
                }
                in 9210..9659 -> {

                    band = "Band = B28 (700MHz) 4G FDD LTE\n"
                }
                in 3450..3799 -> {

                    band = "Band = B8 (900MHz) 4G FDD LTE\n"
                }
                in 1950..2399 -> {

                    band = "Band = B4 (1700MHz) 4G FDD LTE\n"
                }
                in 38650..39649 -> {

                    band = "Band = B40 (2300MHz) 4G TDD LTE\n"
                }
                in 37750..38249 -> {

                    band = "Band = B38 (2600MHz) 4G TDD LTE\n"
                }
                in 600..1199 -> {

                    band = "Band = B2 (1900MHz) 4G FDD LTE\n"
                }
                in 2400..2649 -> {

                    band = "Band = B5 (850MHz) 4G FDD LTE\n"
                }
                in 41590..43589 -> {

                    band = "Band = B42 (3500MHz) 4G TDD LTE\n"
                }
                in 39650..41589 -> {

                    band = "Band = B41 (2500MHz) 4G TDD LTE\n"
                }
                in 5180..5279 -> {

                    band = "Band = B13 (700MHz) 4G FDD LTE\n"
                }
                in 5010..5179 -> {

                    band = "Band = B12 (700MHz) 4G FDD LTE\n"
                }
                in 10562..10838 -> { //Exception du réseau notée avec free ???
                    band = "Band = B1 (2100MHz) 3G UMTS\n"
                }
                else -> band = "Error, EARFCN not associated to our band dictionary"
            }
        }
        if (networkType == "Nr"){ //Type 5G
            when (int) {//5G NR most used bands -----------------
                in 620000..653333 -> {

                    band = "Band = n78 (3500MHz) 5G NR\n"
                }
                in 2054166..2104165 -> {

                    band = "Band = n257 (28GHz) 5G NR\n"
                }
                in 620000..680000 -> {

                    band = "Band = n77 (3700MHz) 5G NR\n"
                }
                in 151600..160600 -> {

                    band = "Band = n28 (700MHz) 5G NR\n"
                }
                in 499200..537999 -> {

                    band = "Band = n41 (2500MHz) 5G NR\n"
                }
                in 2016667..2070832 -> {

                    band = "Band = n258 (26GHz) 5G NR\n"
                }
                in 361000..376000 -> {

                    band = "Band = n3 (1800MHz) 5G NR\n"
                }
                in 693334..733333 -> {

                    band = "Band = n79 (4500MHz) 5G NR\n"
                }
                in 2229166..2279165 -> {

                    band = "Band = n260 (39GHz) 5G NR\n"
                }
                in 342000..357000 -> {

                    band = "Band = n80 (1800MHz) 5G NR\n"
                }
                in 422000..434000 -> {

                    band = "Band = n1 (2100MHz) 5G NR\n"
                }
                in 524000..538000 -> {

                    band = "Band = n7 (2600MHz) 5G NR\n"
                }
                in 460000..480000 -> {

                    band = "Band = n40 (2300MHz) 5G NR\n"
                }
                in 2070833..2084999 -> {

                    band = "Band = n261 (28GHz) 5G NR\n"
                }
                else -> band = "Error, EARFCN not associated to our band dictionary"
            }
        }
        if (networkType == "Wcdma"){ //Type 3G
            when (int) {
                //3G UTMS most used bands -----------------
                in 10562..10838 -> {
                    band = "Band = B1 (2100MHz) 3G UMTS\n"
                }

                in 2937..3088 -> {
                    band = "Band = B8 (900MHz) 3G UMTS\n"
                }

                in 4357..4458 -> {
                    band = "Band = B5 (850MHz) 3G UMTS\n"
                }

                in 9962..9938 -> {
                    band = "Band = B2 (1900MHz) 3G UMTS\n"
                }

                in 1537..1738 -> {
                    band = "Band = B4 (1700MHz) 3G UMTS\n"
                }
                else -> band = "Error, EARFCN not associated to our band dictionary"
            }
        }
        return band
    }

    fun seuilsSpeedTest(ping: Int, dlSpeed: Float, ulSpeed: Float):String{ //Associe les seuils excel avec les réseultats du speed test
        val currentAntenna=listInfos[0]
        var result:String=""
        //Recherche de la bonne technologie puis calculs des seuils
        if(currentAntenna.contains("5G")){
            var parseBW=listInfos[0].split("Bandwidth = ")
            var currentBW= parseBW[1].split(' ')[0].toInt()
            when (currentBW) {
                in 2000000000..2200000000 -> {
                    result = isTestOk(ping,dlSpeed,ulSpeed,2)
                }
                in 3400000000..3600000000 -> {//Technologie 5G 3.5GHz unique ????
                    result = isTestOk(ping,dlSpeed,ulSpeed,1)
                }
            }
        }
        else if (currentAntenna.contains("4G")){
            var parseBW=listInfos[0].split("Bandwidth = ")
            var currentBW= parseBW[1].split(' ')[0].toInt()
            when (currentBW) {
                in 2400000000..2600000000 -> {
                    result = isTestOk(ping,dlSpeed,ulSpeed,3)
                }
                in 2000000000..2200000000 -> {
                    result = isTestOk(ping,dlSpeed,ulSpeed,4)
                }
                in 600000000..900000000 -> {
                    result = isTestOk(ping,dlSpeed,ulSpeed,5)
                }
            }
        }
        else{//3g
            result = isTestOk(ping,dlSpeed,ulSpeed,6)
        } //Cas 2g pas mis en place car impossibilité de recuperer les informations des antennes 2g
        return result
    }

    fun isTestOk(ping: Int,dlSpeed: Float,ulSpeed: Float,id:Int):String{// Mise en place des seuils excel
        when (id) { //Selon la catégorie indiquée en entrée, renvoie les bons seuils
            1 -> { //5G 3.5GHZ
                if(dlSpeed>600 && ulSpeed>60 && ping<30){
                    return "5G (3.5GHz)\nRésultat attendu"
                }else if (dlSpeed>400 && ulSpeed>40 && ping<40){
                    return "5G (3.5GHz)\nRésultat OK"
                }else{
                    return "5G (3.5GHz)\nRésultat NOK"
                }
            }
            2 -> { //5G 2.1Ghz
                if(dlSpeed>85 && ulSpeed>35 && ping<30){
                    return "5G (2.1GHz)\nRésultat attendu"
                }else if (dlSpeed>70 && ulSpeed>25 && ping<40){
                    return "5G (2.1GHz)\nRésultat OK"
                }else{
                    return "5G (2.1GHz)\nRésultat NOK"
                }
            }
            3 -> {//4G 2.5GHz
                if(dlSpeed>95 && ulSpeed>50 && ping<35){
                    return "4G (2.6GHz)\nRésultat attendu"
                }else if (dlSpeed>80 && ulSpeed>40 && ping<55){
                    return "4G (2.6GHz)\nRésultat OK"
                }else{
                    return "4G (2.6GHz)\nRésultat NOK"
                }
            }
            4 -> {//4G 2.1GHz
                if(dlSpeed>85 && ulSpeed>35 && ping<35){
                    return "4G (2.1GHz)\nRésultat attendu"
                }else if (dlSpeed>70 && ulSpeed>25 && ping<55){
                    return "4G (2.1GHz)\nRésultat OK"
                }else{
                    return "4G (2.1GHz)\nRésultat NOK"
                }
            }
            5 -> {//4G 800MHz
                if(dlSpeed>65 && ulSpeed>25 && ping<35){
                    return "4G (800MHz)\nRésultat attendu"
                }else if (dlSpeed>50 && ulSpeed>20 && ping<55){
                    return "4G (800MHz)\nRésultat OK"
                }else{
                    return "4G (800MHz)\nRésultat NOK"
                }
            }
            6 -> {//3G
                if(dlSpeed>14 && ulSpeed>1.8 && ping<56){
                    return "3G\nRésultat attendu"
                }else if (dlSpeed>7 && ulSpeed>0.75 && ping<75){
                    return "3G\nRésultat OK"
                }else{
                    return "3G\nRésultat NOK"
                }
            }
            else -> return "Error"
        }
    }

    //Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Set adapter for our list
        itemAdapter = ItemAdapter(mutableListOf())
        AntennaAllItems.adapter = itemAdapter
        AntennaAllItems.layoutManager = LinearLayoutManager(this)
        //Get the data on start of the app
        initData()

        //Récupération des composants pour afficher le résultat du speedtest
        mTextViewStage = findViewById(R.id.textView_stage)
        mTextViewResult = findViewById(R.id.textView_result)
        mButtonSpeedTest = findViewById(R.id.button_speedTest)

        //Initilisation du SDK SpeedChecker
        SpeedcheckerSDK.init(this)
        SpeedcheckerSDK.SpeedTest.setOnSpeedTestListener(this)

        //Onclick: Lancement du speedtest
        mButtonSpeedTest.setOnClickListener(View.OnClickListener { //TODO: 7. Start Speed test.
            SpeedcheckerSDK.SpeedTest.startTest(this@MainActivity)
            mButtonSpeedTest.isEnabled=false
        })

        //Onclick : Refresh the data des antennes
        RefreshButton.setOnClickListener {
            initData()
        }
    }

    // DEMO APP SPEEDCHECKER : https://github.com/speedchecker/speedchecker-sdk-android/tree/demo-app
    //Liste des fonctions de speedchecker
    //Nous n'avons pas eu le temps de l'implémenter proprement dans une classe
    //Fonctions appelées dans un certain ordre
    override fun onTestStarted() {
        mTextViewStage.text = "Test Started"
        mTextViewResult.text = ""
    }

    override fun onFetchServerFailed() {
        mTextViewStage.setTextColor(Color.parseColor("#d11717"))
        mTextViewStage.text = "Fetch Server Failed"
    }

    override fun onFindingBestServerStarted() {
        mTextViewStage.setTextColor(Color.parseColor("#0FD9F4"))
        mTextViewStage.text = "Finding best server"
    }

    override fun onTestFinished(speedTestResult: SpeedTestResult) { //Resulats du speedtest avec quelques modifications d'affichage (couleur,texte)
        val finalStr = """Server: ${speedTestResult.server.Domain}
        Ping: ${speedTestResult.ping} ms
        Download : ${speedTestResult.downloadSpeed} Mb/s , Upload : ${speedTestResult.uploadSpeed} Mb/s
        """
        if(seuilsSpeedTest(speedTestResult.ping,speedTestResult.downloadSpeed,speedTestResult.uploadSpeed).contains("attendu")){
            mTextViewStage.setTextColor(Color.parseColor("#17d149"))
        }else if(seuilsSpeedTest(speedTestResult.ping,speedTestResult.downloadSpeed,speedTestResult.uploadSpeed).contains("OK")){
            mTextViewStage.setTextColor(Color.parseColor("#e6a40b"))
        }else{
            mTextViewStage.setTextColor(Color.parseColor("#d11717"))
        }

        mTextViewStage.text = seuilsSpeedTest(speedTestResult.ping,speedTestResult.downloadSpeed,speedTestResult.uploadSpeed)
        mTextViewResult.text = finalStr
        mButtonSpeedTest.isEnabled=true
    }

    override fun onPingStarted() {
        mTextViewStage.text = "Ping Test"
    }

    override fun onPingFinished(ping: Int, jitter: Int) {
        mTextViewResult.text = "Ping result : $ping ms | Jitter: $jitter"
    }

    override fun onDownloadTestStarted() {
        mTextViewStage.text = "Download Test"
    }

    override fun onDownloadTestProgress(i: Int, speedMbs: Double, transferredMb: Double) {
        mTextViewResult.text = "$i%/100% \n Speed : $speedMbs Mb/s"//\nTransferredMb: $transferredMb"
    }

    override fun onDownloadTestFinished(v: Double) {
        mTextViewResult.text = "Average Download Speed : $v Mb/s"
    }

    override fun onUploadTestStarted() {
        mTextViewStage.text = "Upload Test "
        //log("Upload Test Started");
    }

    override fun onUploadTestProgress(i: Int, speedMbs: Double, transferredMb: Double) {
        mTextViewResult.text = "$i%/100% \n Speed : $speedMbs Mb/s"//\nTransferredMb: $transferredMb"
    }

    override fun onUploadTestFinished(v: Double) {
        mTextViewResult.text = "Average Upload Speed : $v Mb/s"
    }

    override fun onTestWarning(s: String?) {
        mTextViewStage.text = "Test Warning"
        mTextViewResult.text = s
    }

    override fun onTestFatalError(s: String?) {
        mTextViewStage.text = "Test Fatal Error"
        mTextViewResult.text = s
    }

    override fun onTestInterrupted(s: String?) {
        mTextViewStage.text = "Test Interrupted"
        mTextViewResult.text = s
    }
}