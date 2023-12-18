package com.example.myapplication.data


import com.google.gson.annotations.SerializedName

data class Row(
    @SerializedName("APVCANCELYMD")
    val aPVCANCELYMD: String?,
    @SerializedName("APVPERMYMD")
    val aPVPERMYMD: String?,
    @SerializedName("BPLCNM")
    val bPLCNM: String?,
    @SerializedName("CLGENDDT")
    val cLGENDDT: String?,
    @SerializedName("CLGSTDT")
    val cLGSTDT: String?,
    @SerializedName("DCBYMD")
    val dCBYMD: String?,
    @SerializedName("DTLSTATEGBN")
    val dTLSTATEGBN: String?,
    @SerializedName("DTLSTATENM")
    val dTLSTATENM: String?,
    @SerializedName("LASTMODTS")
    val lASTMODTS: String?,
    @SerializedName("LINDJOBGBNNM")
    val lINDJOBGBNNM: String?,
    @SerializedName("LINDPRCBGBNNM")
    val lINDPRCBGBNNM: String?,
    @SerializedName("LINDSEQNO")
    val lINDSEQNO: String?,
    @SerializedName("MGTNO")
    val mGTNO: String?,
    @SerializedName("OPNSFTEAMCODE")
    val oPNSFTEAMCODE: String?,
    @SerializedName("RDNPOSTNO")
    val rDNPOSTNO: String?,
    @SerializedName("RDNWHLADDR")
    val rDNWHLADDR: String?,
    @SerializedName("RGTMBDSNO")
    val rGTMBDSNO: String?,
    @SerializedName("ROPNYMD")
    val rOPNYMD: String?,
    @SerializedName("SITEAREA")
    val sITEAREA: String?,
    @SerializedName("SITEPOSTNO")
    val sITEPOSTNO: String?,
    @SerializedName("SITETEL")
    val sITETEL: String?,
    @SerializedName("SITEWHLADDR")
    val sITEWHLADDR: String?,
    @SerializedName("TOTEPNUM")
    val tOTEPNUM: String?,
    @SerializedName("TRDSTATEGBN")
    val tRDSTATEGBN: String?,
    @SerializedName("TRDSTATENM")
    val tRDSTATENM: String?,
    @SerializedName("UPDATEDT")
    val uPDATEDT: String?,
    @SerializedName("UPDATEGBN")
    val uPDATEGBN: String?,
    @SerializedName("UPTAENM")
    val uPTAENM: String?,
    @SerializedName("X")
    val x: String?,
    @SerializedName("Y")
    val y: String?
)