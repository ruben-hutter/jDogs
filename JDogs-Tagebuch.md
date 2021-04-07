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
> 1) Wie würde eine ideale projektspezifische Planung aussehen (konkretes Beispiel)?
> 2) Wie können wir den Malus "Human modem" vermeiden?
> 3) Wie soll das Protokoll dokumentiert, im Code implementiert und validiert werden?
> 4) Wie soll das Software-Qualitätssicherungskonzept konkret aussehen?
> 5) Was ist genau mit "Garbage" gemeint?

---

*24.März.2021 10:15, Johanna*

### Protokoll#4 - Übungsstunde am 24.03.2021

Antworten auf unsere Fragen:
> 1) Der Tutor stellt uns einen Screenshot eines guten Projektplans zur Verfügung. Aus dem Projektplan sollte ersichtlich sein, um was für ein Spiel es sich genau handelt, etc.
> 2) Auf der Kommandozeile sollten nicht die exakten Befehle eingegeben werden, stattdessen sollte ein Frage-Antwort-Spiel gestartet werden, z.B. "Willst du deinen Nickname ändern?" "Gewünschter neuer Nickname", etc.
> 3) Dokumentation in einem PDF-Dokument. Implementation als enum, siehe Vorlesungsfolien. Validierung: Es sollen nur die definierten Befehle akzeptiert werden. Bei ungültigen Befehlen sollen entsprechende Handlungen eingeleitet werden.
> 4) Beschreibung, wie wir sicherstellen wollen, dass unsere Software das macht, was wir wollen. Auflistung der Tools und Hilfsmittel.
> 5) Der Server soll damit umgehen können, wenn nicht definierte Befehle gesendet werden.

Wir verteilten die anstehenden Aufgaben wie folgt:
> - Gregor: Ping/Pong so abändern, dass das Ping regelmässig gesendet wird.
> - Ruben: Implementiert die gestern festgelegten Befehle des Protokolls.
> - Johanna: Qualitätssicherungskonzept erstellen. Frage-Antwort-Spiel bei einigen Befehlen einbauen.

**Nächstes Treffen:** Freitag, 26.03.2021, 20 Uhr. Bis dann sollten die Aufgaben mehrheitlich erledigt sein. Kleinere Korrekturen können am Samstag erfolgen.

---

*25.März.2021 22:30, Ruben*

### Bericht#03 - Implementierung des Protokolls

Zwischen gestern und heute habe ich ein Protokoll für unser Projekt erstellt und begonnen, es in den bereits vorhandenen Code für den Client und den Server zu integrieren. Was ich jetzt noch fertigstellen muss, ist die vollwertige Integration, die die eingefügten temporären Befehle ersetzt.
Anfangs haben wir auch einen Unterschied zwischen Nickname und Benutzername gemacht, den wir jetzt auf nur den Benutzernamen ändern werden; das wird die Implementierung sicherlich vereinfachen und weniger verwirrend machen.
Die Schwierigkeit, auf die ich bei diesem Prozess gestoßen bin, war, dass, obwohl wir uns den Code gemeinsam angeschaut haben, er immer noch anders geschrieben war, als ich es getan hätte, so dass ich oft doppelt überprüfen musste, was die verschiedenen Teile des bereits existierenden Codes taten. Aus dieser Erfahrung habe ich gelernt, wie wichtig es ist, seinen Code gut zu dokumentieren.

---

*26.März.2021 20:00, Johanna*

### Protokoll#05 - Protokoll

An der heutigen Sitzung besprachen wir kurz das Qualitätssicherungskonzept. Danach machten wir uns gemeinsam Gedanken, wie wir das Protokoll genau implementieren und verwenden können. Bis morgen recherchieren wir nach möglichen Lösungen. Es war uns nicht ganz klar, wo wir die Befehle des Protokolls behandeln sollen, im Enum, wo auch die Protokollbefehle definiert sind, oder im MessageHandler.

**Nächstes Treffen:** Samstag, 27.03.2021, 17 Uhr

---

*27.März.2021 17:00, Johanna*

### Protokoll#06 - Protokoll

Bei unserem heutigen Treffen einigten wir uns darauf, dass die Behandlung der Protokollbefehle im jeweiligen MessageHandler vonstatten gehen sollte und nicht in Protocol.java. Danach gingen wir die Achievements für Milestone 2 durch. Dabei stellten uns Emoijs im Chat vor einige Probleme. Zwischen Linux und Mac konnten Sonderzeichen und Emoijs problemlos versendet werden, bei Windows wurden hingegen diese Zeichen nicht korrekt dargestellt. Wir versuchten, das Problem zu beheben. Während des Testens wurden wir auf zwei weitere Fehler aufmerksam, die während der Zoom-Sitzung behoben wurden.
>1. Wenn ein Client seinen Namen ändert, zählt der Server dies als eine neue Verbindung
>2. Wenn ein Client seinen Namen ändert, wird der neue Name zur Liste der aktiven User hinzugefügt.

Wir sprachen noch über AWT, SWING und JavaFX, die in der Vorlesung zu GUI vorgestellt wurden, und entschieden uns, JavaFX zu verwenden.

---

*28.März.2021 16:30, Gregor*

### Bericht#04 - Aufbau der ServerClient-Architektur

Ich habe viel an der ServerClient-Architektur unserer Gruppe gearbeitet und werde hier einige wichtige Punkte der Architektur besprechen.

Um uns dem Problem anzunähern, können wir uns überlegen, welche Funktionen Server- und Clientseite jeweils erfüllen müssen. Diese sind auf beiden Seiten fast dieselben.

Die erste Funktion ist, Input zu empfangen.
Die Clientseite hat zwei verschiedene Inputs, die erfasst werden müssen.
Erstens alles, was von der Tastatur eingeben und (später) auch, was auf Buttons des GUI gedrückt wird, sowie zweitens alles, was vom Server zum Client gesendet wird.

Die zweite Funktion ist, Input zu verarbeiten.
Die Clientseite muss diese Inputs verarbeiten, auch dafür ist ein Thread nötig.
Um die Belastung der CPU gering zu halten, könnte man sich überlegen diesen Thread nur dann zu aktivieren, wenn Input empfangen wurde.

Die dritte Funktion ist, Output zu verschicken.
Auf Clientseite kann dieser Output erstens zum User gesendet werden, um etwas im GUI bzw. in der cmd anzuzeigen und kann zweitens zum Server gesendet werden.

Die vierte Funktion ist, die Verbindung zwischen Server und Client zu überwachen und bei Verbindungsabbruch, Schritte einzuleiten.
Das wird momentan auf Server- wie Clientseite durch einen eigenen Thread ausgeführt. Wir wollen allerdings, um auch da ein wenig CPU-Leistung einzusparen, diesen Thread nur noch alle 5 Sekunden laufen lassen mittels eines Threadpools.

Auf Serverseite läuft alles ganz ähnlich ab, aber es gibt für jeden Client eine eigene Input-Entgegennahme, eine eigene Verarbeitung von Input, eine eigene Output-Linie sowie eine eigene Überprüfung der Verbindung.
Das heisst, jedes Mal, wenn sich ein neuer Client mit dem Server verbindet, werden 4 neue Threads gestartet(bzw. 3 und der vierte, der Überwachungsthread, wird regelmässig aktiviert).

Deshalb hat der Server eine Funktion mehr, die überhaupt erlaubt, all diesen Clients auf Serverseite 4 (bzw. 3 Threads) zuzuweisen.
Diese Funktion wird im Main-Thread(public static void main(String[] args)) ausgeführt und ein while(true) - Loop sorgt dafür, dass der Server immer bereit ist, neuen Clients, 4(bzw. 3) Threads auf Serverseite zuzuweisen.

Ausblick

Alle Nachrichten von Server zu Client bzw. von Client zu Server gehen durch dieselbe Verbindung.
Da sind Spielbefehle, Nicht-Spielbefehle, öffentliche Chat-Nachrichten, private Chat-Nachrichten, Falsch-Nachrichten und Ping-Pong-Signale. Und nach Ankunft muss für eine schnelle Verteilung gesorgt werden, um Verzögerungen zu minimieren.
Der nächste Schritt, den wir nun angehen müssen ist, liegt genau da: wir müssen für die richtige Verteilung der Nachrichten sorgen. Und wir benötigen eine kluge Struktur, um die Befehle, die die Nachrichten nach sich ziehen, auszuführen.

---

*30.März.2021 19:00, Gregor*

### Protokoll#07 - Protokoll

Thema war zuerst kurz einige Veränderungen, die ich hochlud und danach sprachen wir über Meilenstein 3.
Der grösste Brocken ist unserer Ansicht nach die GameLogic. Dort müssen wir viel Zeit investieren. Deshalb ist es wichtig, andere kleinere Sachen schnell fertig zu bringen und zugleich schon mit der GameLogic zu beginnen.
Als wichtige kleinere Sachen wären die Qualitätssicherung sowie das GUI und der Whisper-Chat zu nennen.

Heute ging es darum, die Aufgaben zu verteilen und einen ersten Überblick über die GameLogic zu gewinnen. Die GameLogic sollte Brett, Karten, Spielfiguren und eine Klasse o.ä., die die Veränderungen berechnet und überprüft. Momentan möchten wir die Zustände in den Spielfiguren speichern. Wir werden sehen, ob das der richtige Weg ist.

Bis zum nächsten Treffen wird sich Ruben um die GameLogic kümmern, Johanna schaut sich die Konzepte zur Qualitätssicherung(JUnit-Test, Mockito..) an, um herauszufinden, wie wir sie benutzen können, Gregor kümmert sich um den WhisperChat und die GUI.

---

*03.04.2021 10:00, Johanna*

### Protokoll#08 - Protokoll

Gregor berichtete von einem grösseren Problem, das bei ihm in Zusammenhang mit Guava Eventbus auftrat und es ihm praktisch verunmöglichte, weiter an der GUI zu arbeiten. Wir sprachen noch über JUnit-Tests und die Game-Logic. Uns ist noch nicht ganz klar, wie wir JUnit-Tests für komplexere Aufgaben anwenden können. 
Gregor versucht das Problem mit Eventbus zu lösen. Ruben und Johanna versuchen sich am Nachmittag im Pair-Programming an einer Implementation des Spielbretts und der Positionen der Murmeln. 
Bericht vom Nachmittag (Spielbrett): Wir überlegten, ob wir das Spielbrett als Murmel-Array darstellen oder ob das Spielbrett getrennt von den Murmeln dargestellt werden sollte. Wir entschieden uns für Letzteres.


 
