# Cloud build iš telefono (be kompiuterio)

Šis projektas turi **GitHub Actions** darbo eigą, kuri **sukompiliuos APK** debesyje ir pateiks atsisiunčiamą artefaktą.

## Kaip gauti APK tik su telefonu

1. **GitHub programėlėje ar naršyklėje** sukurkite naują repo (pvz., `WordCounterApp`).
2. Įkelkite visą šio projekto turinį (įskaitant `.github/workflows/build-apk.yml`).
   - GitHub mobilioje naršyklėje galima įkelti ZIP ir jį **Upload files** (arba atskirus failus).
3. Į `main` / `master` šaką įkelus failus, automatiškai startuos **Actions: “Build APK (Debug)”**.
4. Atsidarykite repo → **Actions** → pasirinkite bėgantį / įvykusį “Build APK (Debug)” → **Artifacts** →
   atsisiųskite **WordCounterApp-debug-apk** → `app-debug.apk` į telefoną.
5. Įdiegimas: telefone atidarykite `app-debug.apk` → **Allow from this source** (jei paprašys) → Install.

> Pastaba: tai „debug“ APK (skirtas testavimui). Kai nuspręsite kelti į Google Play, sukursime **.aab** ir pasirašysime raktu.