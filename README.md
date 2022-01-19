# Projet Recherche et innovation
## _LockingBand App_

Notre PRI se base sur un projet de SNEF Telecom proposant de travailler sur une application mobile en Kotlin
Notre application possède les fonctionnalités suivantes :
- [x] Liste les atennes que le telepohne capte
- [x] Donne des infomations sur ces antennes
    -  Son PCI (identifiant)
    - Son EARFCN (identifiant la bande utilisée et sa technologie)
    -  Sa bande passante (pour les technologies 4G et 5G)
    - Sa puissance (en dBm)
-  [x] Effectuer un SpeedTest et classer les données de ce dernier selon différents seulis fournis
- [ ]  La possibilité de verouiller le telephone sur une bande/Pci particulier
Ce dernier point n'ayant pas pu être réalisé, nous avons récupéré des informatinos dans une documentation sur des moyens de réaliser le locking band.

## Technologies

Notre projet a été réalise sur android studio en langage Kotlin.


## Installation
L'installation peut se faire via android sutido en installant notre applicaiton sur le telephone.
Le Samsung Galaxy A32 5G qui nous a été fourni possède la dernière version de notre application installée (PRI_LockingBand_app)

Configuration de notre application (build.gradle) :
```sh
android {
    compileSdkVersion 30
    buildToolsVersion "30.0.2"

    defaultConfig {
        applicationId "com.example.pri_LockBand"
        minSdkVersion 21
        targetSdkVersion 30
        versionCode 1
        versionName "1.0"
    }
```

## Developers

- GAGNAIRE Thomas
- Biron Gregoire
- Abdou Amine
- Bakalemian Thomas


#### Informations supplémentaires
Le parsing des données des antennes se fait différent pour chaque technologie. Il est donc important de vérifier la syntaxe si l'on parse a la main.
Le speedtest checker a été implémenté mais n'a pas pu être mis "proprement" dans une classe
Les seuils fournis pour la 2G ainsi que l'affichage des antennes 2G n'ont pas pu être réalisés a cause des difficultés a capter des antennes 2G.

#### Liens Utiles
- https://halberdbastion.com/technology/cellular/4g-lte/4g-lte-frequency-bands : Dictionnaire utilisé pour assoissier les bandes a l'EARFCN
- https://developer.android.com/reference/android/telephony/TelephonyManager : Bibilotheque android utilisées pour obtenir les données
- https://github.com/speedchecker/speedchecker-sdk-android/tree/demo-app : SDK utilisé pour le speedtest
