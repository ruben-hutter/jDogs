# JDogs-Tagebuch

---

*10.März.2021 09:45, Ruben*

### Protokoll#01 - Spielideen

Heute haben wir uns zum ersten Mal getroffen, und jeder hat seine Ideen für das Spiel vorgestellt. Wir haben dann eine erste Abstimmung durchgeführt, um zu sehen, welche der Vorschläge in Frage kommen, und das Ergebnis war wie folgt:

Spiel | Stimmen
----- | -------
Brändi Dog | 3
Das verrückte Labyrinth | 4
Jet Fighter | 3

(Jeder hatte 2 Stimmen zur verfügung)

Nach einer Analyse der möglichen Probleme der verschiedenen Spiele (z.B. Unterschiede zwischen Echtzeit- und rundenbasiertem Spiel) haben wir uns für Brändi Dog entschieden.
Wir haben uns für ein regelmäßiges wöchentliches Treffen entschieden, um Fortschritte und Probleme zu besprechen, die Planung zu überprüfen usw.
Zum Abschluss haben wir noch die verschiedenen Aufgaben für Meilenstein 1 aufgeteilt.
Ein paar Stunden später mussten wir die Aufgabenverteilung ändern, da Tamer das Projekt leider verlassen musste.

Aufgabe | Wer?
------- | ----
Presentation: About a Game | Gregor & Johanna
Presentation: About a Game | Gregor & Johanna
Product: Ahead of Schedule and Under Budget | Johanna & Gregor & Ruben (gemäss Stand der Arbeiten)
Process: Dear Diary.. | Ruben
Process: At least one entry in the diary | Ruben
Presentation Mockup | Gregor & Johanna
Presentation: Networking | Johanna
Presentation: Requirement Analysis | Johanna & Gregor
Presentation: Who? What? When?: Project timeline and responsibilities | Ruben

---

*12.März.2021 14:00, Ruben*

### Bericht#01 - Erste Vortschritte

Johanna und Gregor haben die Software Requirements sowie die Netzwerkanalyse für JDogs diskutiert. Ausgehend von den Anforderungen, die in den Meilensteinen vorgegeben sind, haben wir uns überlegt, was unser Spiel sonst noch können soll und welche Features speziell bei unserem Spiel wichtig sind. Einige dieser speziellen Anforderungen, die wir an die Software von JDogs stellen, sind z.B.: rundenbasiertes Spiel mit festgelegter Spielerreihenfolge, Spielbrettgrösse auswählen, der Whisper Chat muss während des eigentlichen Spiels deaktiviert sein, etc. (siehe Folien). Bei der Netzwerkanalyse war uns nicht sofort klar, wie detailliert die Funktionalitäten von Client und Server aufgelistet und wie diese angemessen dargestellt werden sollen. Wir entschieden uns schliesslich dafür, das Spiel in vier Phasen einzuteilen (vor dem Spiel, Spielbeginn, während des Spiels, Spielende) und jeweils einige exemplarische Funktionalitäten aufzulisten, die speziell für das Spiel JDogs wichtig sind (siehe Folien).

Ich habe eine erste Version des GanttProjekts erstellt, basierend auf den Anforderungen für die verschiedenen Meilensteine. Neben der rechtzeitigen Terminierung von den Aufgaben (Milestones) habe ich über eine personelle Terminplanung nachgedacht, so dass z.B. für jede Woche eine andere Person für die Kontrolle des Tagebuchs zuständig ist.
Parallel dazu habe ich auch mit der Erstellung des oben erwähnten Tagebuchs begonnen und darin die ersten Notizen und Protokolle der letzten Tage eingefügt. Ich habe mich für eine Markdowndatei entschieden, weil ich denke, dass dies der beste Kompromiss zwischen Bequemlichkeit (von jedem Texteditor öffenbar) und Lesbarkeit ist. Außerdem finde ich es auch eine gute Praxis für zukünftige Projekte, sowie für GitHub.
In den kommenden Tagen werde ich den Arbeitsplan noch etwas anpassen und, anhand der von Johanna und Gregor durchgeführten Analysen (Netzwerkanalyse und Software Requirements), die notwendigen Details hinzufügen.

---

*14.März.2021 13:00, Ruben*

### Protokoll#02 - Update Milestone 1

Heute haben wir uns die Präsentation zusammen angeschaut und entschieden, welche Punkte wir bis Dienstag verbessern müssen. Dann haben wir das Mockup und das GanttProjekt besprochen; was uns anfangs einige Schwierigkeiten bereitet hat, da wir es nicht auf Git hochladen konnten. Nach mehreren Versuchen haben wir es dann geschafft, es auch als .xml und nicht nur als .csv hochzuladen.
Unser nächstes Meeting findet am Dienstag statt, bei dem wir eine Generalprobe der Präsentation machen werden. Bis dahin stellt Gregor das Mockup fertig und korrigiert die besprochenen Punkte der Präsentation. Johanna und ich bearbeiten das GanttProjekt.

> Fragen für Übungsstunde:
>
> - GanttProject to PDF?
> - Tagebuch to PDF?
> - Muss das README.md für Milestone 1 schon richtig sein?

---

*15.März.2021 21:00, Ruben*

### Bericht#02 - Umsetzung Korrekturen

Gregor hat das Mockup fertiggestellt und ein Grundkonzept für das Spielfeld erstellt.
Ich habe weiter an der Planung gearbeitet. Die größte Schwierigkeit ist im Moment die Struktur des gesamten Projekts. Da wir noch nie ein so großes Projekt durchgeführt haben und vor allem gerade erst einige grundlegende Konzepte gelernt haben, wie z.B. die Client-Server-Beziehung, ist es selbst mit den Milestones als Referenz schwierig, das zeitliche Verhältnis der verschiedenen Punkte zu erkennen und auch die Zeit für eine bestimmte Aufgabe zu schätzen.

---

*23.März.2021 15:30, Johanna*

### Protokoll#3 - Update Milestone 2

Gregor hat seit unserem letzten Treffen bereits ein Grundgerüst für den Chat, Login und Ping/Pong erstellt. Ruben hat sich Gedanken zum Protokoll gemacht, ich zur Qualitätssicherung. Zu Beginn der heutigen Sitzung erklärte Gregor den anderen Teammitgliedern seinen Code. Gemeinsam schauten wir uns den Code an, klärten Fragen und brachten Verbesserungsvorschläge ein. Wir testeten den Chat unter Zuhilfenahme von Hamachi. Danach gingen wir die Achievements für Milestone 2 durch und notierten uns, was wir noch in der Übungsstunde fragen möchten. Zuletzt überlegten wir uns gemeinsam einige Befehle für das Netzwerkprotokoll, welche Ruben in einem Dokument festhielt. Wir einigten uns darauf, für die Befehle vorerst 4 Buchstaben zu verwenden. 

> Fragen für die Übungsstunde:
>
> - Wie würde eine ideale projektspezifische Planung aussehen (konkretes Beispiel)?
> - Wie können wir den Malus "Human modem" vermeiden? 
> - Wie soll das Protokoll dokumentiert, im Code implementiert und validiert werden? 
> - Wie soll das Software-Qualitätssicherungskonzept konkret aussehen? 
> - Was ist genau mit "Garbage" gemeint? 

