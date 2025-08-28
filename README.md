# WordCounterApp (Android, offline)

Ši aplikacija skaičiuoja apytikslį per dieną pasakytų žodžių kiekį ir rodo grafiką (dienos ir valandų pjūviai). Veikia **fone** per `ForegroundService` ir be interneto (naudoja tik vietinį garso apdorojimą).

## Kaip paleisti (žingsnis po žingsnio)
1. Atsisiųskite šį ZIP ir išskleiskite.
2. Atidarykite **Android Studio** → *Open* → pasirinkite `WordCounterApp` katalogą.
3. Leiskite `Gradle Sync` užsibaigti (reikės interneto tik pirmam priklausomybių užsikrovimui).
4. Pasirinkite **Run** ir įdiekite į telefoną (Android 8+).
5. Paleidus, suteikite leidimus: mikrofonas (+ pranešimai Android 13+).
6. Ekrane paspauskite **START** ir kalbėkite – skaičius „Šiandien“ pradės augti. **STOP** – sustabdo.
7. Grafike „Paskutinės 30 dienų“ matysite dieninius duomenis, apačioje – pasirinktos dienos valandinį grafiką.
8. Nustatymuose sureguliuokite **žodžiai/sekundę** slankiklį. Numatytas 2.5 (≈150 wpm).

## Tik mano balsas
- Įjunkite „Skaičiuoti tik mano balsą (eksperimentinė)“. Tada paspauskite **START**, pakalbėkite ~10 s, **STOP** – taip aplikacija susikurs paprastą „balso parašą“. (Ši demonstracinė implementacija naudoja MFCC ir kosinio panašumo slenkstį – ne tobula, bet padeda atmesti pašalinius balsus.)

## Privatumas
- Nekaupiamas ir neišsaugomas jokios frazės tekstas. Į DB rašomas tik laiko momentas ir **žodžių skaičius**.
- Duomenys laikomi 30 dienų, seni automatiškai trinami.

## Pastabos
- Kadangi nenaudojamas debesinis atpažinimas, **žodžių skaičius yra apytikslis**: skaičiuojamas pagal kalbos segmento trukmę × jūsų nustatytą „žodžiai/sek“. Norėdami tikslesnio skaičiavimo, galima integruoti **Vosk** (offline ASR) modelį – struktūra tam paruošta (žr. TODO skiltyje).
- Patikimam veikimui rekomenduojama išjungti „Battery optimization“ programėlei (Settings → Apps → WordCounter → Battery → Allow background activity).

## Leidimas į Google Play
- Pridėkite **privatumo politiką** (aprašykite, kad nerenkamas tekstinis turinys, tik agreguoti skaičiai).
- Iškompiliuokite **.aab** (Build → Build App Bundle).
- Užpildykite Data safety formą ( pažymėkite, kad naudojamas mikrofonas ir duomenys lieka įrenginyje ).

## TODO (jei norėsite toliau plėtoti)
- Įdėti **Vosk** offline modelį tiksliam žodžių skaičiui (be teksto saugojimo).
- Pagerinti VAD (pvz., WebRTC VAD) ir atskiriamąją gebą triukšme.
- Atskiras „Balso parašo“ vedlys su tiesioginiu įrašymu UI.
- Widget‘as su „Šiandienos žodžiai“.