# Projet Recherche et innovation
## _LockingBand App_

Notre PRI se base sur un projet de SNEF Telecom proposant de travailler sur une application mobile en Kotlin
Notre application possède les fonctionnalités suivantes :
- [x] Liste les antennes que le télépohne capte
- [x] Donne des infomations sur ces antennes
    - Son PCI (identifiant)
    - Son EARFCN (identifiant la bande utilisée et sa technologie)
    - Sa bande passante (pour les technologies 4G et 5G)
    - Sa puissance (en dBm)
-  [x] Effectuer un SpeedTest et classer les données de ce dernier selon différents seuilis fournis
- [ ]  La possibilité de verouiller le telephone sur une bande/Pci particulier
Ce dernier point n'ayant pas pu être réalisé, nous avons récupéré des informations dans une documentation sur des moyens de réaliser le locking band.

## Technologies

Notre projet a été réalisé sur android studio en langage Kotlin.
Nous utilisons une librairie android "TelephonyManager" afin de récupérer les infromations liées au telephone. Ici, les données qui nous intéressent sont celles du réseau et particulièrmeent les antennes captées par le téléphone.
Nous utilisons un SDK externe demandé par notre porteur de projet dans l'application nommé SpeedChecker

## Installation
L'installation peut se faire via android studio en installant notre application sur le téléphone.
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

- GAGNAIRE Thomas thomas.gagnaire@telecom-st-etienne.fr
- Biron Gregoire gregoire.biron@telecom-st-etienne.fr
- Abdou Amine amine.abdou@telecom-st-etienne.fr
- Bakalemian Thomas thomas.bakalemian@telecom-st-etienne.fr


#### Informations supplémentaires
__ATTENTION__ : En cas de nouvelle installation du programme, veuillez penser a donner les autorisations dans les paramètres de l'application (LOCALISATON & TELEPHONE)
L'autorisation "Activité physique" est présente depuis l'ajout du SpeeedTest (mais apparement pas nécessaire au projet)
Le speedtest n'est pas capable de donner les informations sur l'antenne connectée, nous avons donc codé cela nous meme en utilisant les informations de la premeire antenne de la liste (celle dont on est connecté). Cela implique que le speedtest, utilisé en wifi, affichera quand même une connection de type données mobiles alors que ce n'est pas le cas.
Le parsing des données des antennes se fait différement pour chaque technologie. Il est donc important de vérifier la syntaxe si l'on parse à la main (pour ajouter des informations) pour chaque technologie.
Le speedtest checker a été implémenté mais n'a pas pu être mis "proprement" dans une classe, l'ensemble des classes dont se sert le listener sont encore dans le "main".
Les seuils fournis pour la 2G ainsi que l'affichage des antennes 2G n'ont pas pu être réalisés à cause des difficultés a capter des antennes 2G qui se font rares.

#### Liens Utiles
- https://halberdbastion.com/technology/cellular/4g-lte/4g-lte-frequency-bands : Dictionnaire utilisé pour assoissier les bandes a l'EARFCN
- https://developer.android.com/reference/android/telephony/TelephonyManager : Bibilotheque android utilisées pour obtenir les données
- https://github.com/speedchecker/speedchecker-sdk-android/tree/demo-app : SDK utilisé pour le speedtest
