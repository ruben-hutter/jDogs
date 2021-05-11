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

**Nächstes Treffen:** Montag, 05.04.21, 16:30 Uhr

---

*05.04.2021 16:00, Johanna*

### Protokoll#09 - Protokoll

Ruben zeigte uns seine Implementation des Spielbretts. Das Spielbrett sowie die Position der Spieler kann auf der Konsole ausgegegeben werden. Wir diskutierten,  ob die Farbe der Karten nötig ist, oder ob der Kartenwert reicht. Wir überlegten, welche externen Libraries wir benutzen könnten und entschieden, dass sich Gregor Log4j, Ruben Guava und Johanna Mockito genauer ansehen soll. Gregor macht sich Gedanken zur Regelübrprüfung. 

**Nächstes Treffen:** Mittwoch, 07.04.21 in der Übungsstunde

---

*07.04.2021 10:00, Johanna*

### Protokoll#10 - Protokoll

Gregor konnte das Problem mit Guava Eventbus glücklicherweise lösen. In der Übungsstunde besprachen wir das Spielbrett, die Karten, GUI und die Anforderungen für den Meilenstein 3.
- Spielbrett: Wir diskutierten, wie wir das Spielbrett und die Spielfiguren konstruieren sollen, damit wir jeweils den aktuellen Stand der Figuren abfragen können. Gregor schlug vor, eine abstrakte Klasse Tiles zu erstellen,  von der dann die Heaven Tiles, Home Tiles und Track Tiles abgeleitet werden. 
- Karten: Gregor schlug vor, eine abstrakte Klasse Card zu erstellen, von der dann für jede Karte eine eigene Klasse abgeleitet werden kann.Um den Spielzug zu überprüfen reicht es, wenn lediglich der Wert der Karte übergeben wird. Spezialkarten sind Joker und Ass, die verschiedene Werte einnehmen können. 
- GUI: Ruben schlug vor, dass ein WhisperChat mit einer anderen Person gestartet werden kann, wenn das Zeichen @ verwendet wird. 


Wir fragten Tim, was man alles mit JUnit-Tests testen sollte. Er meinte, hauptsächlich sollte die GameLogic getestet werden, die Verbindung zwischen Client und Server sei schwierig zu testen. 

**Nächstes Treffen:** Freitag, 09.04.21, 10 Uhr. 

Bis dahin kümmert sich Gregor weiter um GUI, GameRules und Logger, Ruben um das Spielbrett und Guava und Johanna um die Karten und Mockito.

---

*09.04.2021 10:00, Johanna*

### Protokoll#11 - Protokoll

Bei der heutigen Sitzung wurden u.a. die Themen Spielbrett, Spielorganisation und Karten besprochen.
- Spielbrett und -figuren (Pieces): Es kam die Frage auf, ob die HomeTiles überhaupt als Tiles dargestellt werden sollen, da es für die Figure zu Hause keine Rolle spielt, auf welcher Position im Haus sie stehen. Weiterhin ist es wichtig, zu speichern, ob eine Figur das erste Feld nach dem Start blockiert oder nicht (hasMoved).
- Spielorganisation: Gregor kreierte ein Objekt GameFile, welches die Informationen enthält, die nötig sind, um ein Spiel zu beginnen (GameID, Host, Nicknames der Spieler). Mit dem neu eingeführten Befehl OGAM (Open Game) kann ein Spiel eröffnet werden, wenn die Bedingungen erfüllt sind (z.B. korrekte Spieleranzahl). Wir diskutierten, wie festgestellt werden kann, wann neue Karten verteilt werden sollten. Es reicht nicht aus, nur zu kontrollieren, ob alle Spieler keine Karten mehr haben, denn es könnte sein, dass ein Spieler noch Karten hat, diese aber nicht spielen kann. Gregor schlug vor, eine zufällige Karte von der Spielerhand abzuziehen, wenn ein Spieler keine Karte legen kann. 
- Karten: Wenn eine Karte gespielt wird, sollen neben dem Befehl, dass eine Karte gespielt wird, folgende Infos übermittelt werden: Name der Karte; Spielfigur, auf die die Karte angewendet werden soll; Anzahl Schritte, die die Spielfigur laufen respektive die Aktion, die durchgeführt werden soll. Bsp: PCAR KING max-1 13. 
- Protokoll: Wir legten als Seperator den Leerschlag fest. 
- Libraries: Wir denken, Log4j wäre sicherlich äusserst hilfreich, aber wir wissen noch nicht genau, wie wir diesen Logger in unserem Projekt konkret einsetzen können. 

**Nächstes Treffen:** Dienstag, 13.04.21, 15:30 Uhr. 

---

*13.04.2021 15:30, Johanna*

### Protokoll#12 - Protokoll

Gregor und Ruben berichteten von ihrem PairProgramming vom Vormittag. Sie haben damit begonnen, den in den letzten Tagen erstellten Code zusammenzufügen, um den Spielbrettteil und die Karten einzubinden und alles zwischen dem Server und den Clients in Verbindung zu bringen. Auf diese Weise kann das Spiel, das bisher nur lokal zu sehen war, nun mit einem Befehl über die Konsole gestartet werden. Auf diese Weise startet das Spiel auf dem Server, der eine Nachricht an die beteiligten Clients (die sich in der für dieses Spiel eingerichteten Lobby befanden) sendet, so dass die verschiedenen Clients das Spiel ebenfalls sehen und mit dem Spielen beginnen können.
Wir haben dann eine Frist am Freitag um 10:00 Uhr gesetzt, um zu überprüfen, ob das Spiel richtig funktioniert, und um mögliche Fehler zu beheben. Die verbleibenden Tage bis zur Auslieferung sind für die Fertigstellung von Tests, Dokumentation und Kommentaren im Code sowie für die Vorbereitung der Präsentation vorgesehen.
Ein Thema war auch die ungleichmässige Arbeitsverteilung bei Milestone 3. Für die Zukunft möchten wir noch ein zusätzliches Meeting pro Woche festlegen.

**Nächstes Treffen:** Mittwoch, 14.04.21, 09:00 Uhr. 

---

*14.04.2021 - 18.04.21, Johanna*

### Protokoll#13 - Protokoll

Am Freitag, Samstag und Sonntag stellten wir das Spiel fertig für den Meilenstein 3. Gemeinsam testeten wir das Spiel und teilten die anstehenden Arbeiten auf. Wir fanden verschiedene Fehler, die noch korrigiert werden konnten: z.B. wurde die gespielte Karte nicht von der Hand des Spielers abgebucht, etc. 

---

*20.04.2021, Johanna*

### Protokoll#14 - Protokoll

Heute bereiteten wir die Präsentation vor. Wir gingen die einzelnen Punkte durch (About A Game, Demo, Progress Report, QA, Rules To Code, Technology) und gestalteten die passenden Folien dazu. Zudem überlegten wir uns einen Zeitplan für die Präsentation. Um für die morgige Übungsstunde vorbereitet zu sein, testeten wir, ob JDogs auch über Hamachi spielbar ist. 

**Nächstes Treffen:** Mittwoch, 21.04.21, 09:45 Uhr. 


---

*21.04.2021, Johanna*

### Protokoll#15 - Protokoll

Leider konnten wir das Spiel nicht so wie geplant in der Übungsstunde vorzeigen, es gab Probleme bei der Erstellung eines Servers mit einem anderen Port als 8090. Wir durften das Problem bis am Nachmittag beheben. Zudem stellten wir die Präsentation für Meilenstein 3 fertig. Anschliessend besprachen wir das weitere Vorgehen. Wir möchten so schnell wie möglich die Spiellogik komplett implementiert haben, das bedeutet konkret, dass die Karten 7, Jack und 4 ihre volle Funktionalität erhalten und dass ein Spieler, der gerade aus dem Zwinger gekommen ist, den Weg für andere Figuren blockiert. Die Hauptaufgabe bei Meilenstein 4 scheint uns die Grafik zu sein. Wir diskutierten darüber, wie wir das Spiel komplett über die grafische Benutzeroberfläche laufen lassen können (z.B. auswählen der Figuren, Karten und Zielpositionen durch klicken). 

**Nächstes Treffen:** Donnerstag, 22.04.21, 09:30 Uhr: Generalprobe Präsentation.

**Übernächstes Treffen:** Montag, 26.04.21, 18:00 Uhr

---

*26.04.2021, Ruben*

### Bericht#05 - GameLogic debugged

In den Tagen seit dem letzten Milestone habe ich GameLogic umgeschrieben und eine Klasse hinzugefügt, die die verschiedenen Checks übernimmt, so dass die Klasse, die Spielbefehle von Clients empfängt, weniger überfüllt ist. Ich habe auch die Fehler korrigiert, die beim Ausführen von Sonderzügen mit den Karten -4, 7 und Bube auftraten. Mein Ziel ist es, in den nächsten Tagen noch die Möglichkeit hinzuzufügen, im Team zu spielen, also die Möglichkeit, mit den Spielsteinen des Verbündeten mit der Karte 7 zu spielen, und das Spiel mit dem Partner beenden zu können, wenn man seine eigenen Murmeln in Sicherheit gebracht hat.

---

*26.04.2021, 18 Uhr, Johanna*

### Protokoll#16 - Protokoll

Gregor zeigte uns seine erste Version der grafischen Umsetzung des Spiels. Man kann die gewünschte Karte, die Spielfigur und die Zielposition durch klicken auswählen und mit dem Button "Make move" bestätigen. Ruben hat die GameLogic soweit fertiggestellt, dass die Karten Jack, 7 und 4 sowie das Blockieren korrekt funktionieren. Es kamen folgende Punkte zur Sprache:
- Karte ACEE: Nicht ACE1 resp. AC11 übergeben, sondern ACEE, analog zur Karte 4.
- Fenster bei New Game: Das Fenster, um ein neues Spiel zu erstellen, flimmert. 
- SURR in GUI: In der GUI muss noch die Möglichkeit eingebaut werden, die Karten für eine Runde abzulegen.  
- Startbutton in Lobby: Momentan kann man mit dem Starbutton in der Lobby noch kein Spiel starten.
- Chat in GUI: Der Chat wird noch nicht in der Spiel-GUI angezeigt, nur in der Lobby.
- Nummerierung der Piece-IDs: Ruben hat die Piece-IDs in der GameLogic von 1-4 nummeriert. Gregor hat die Spielfiguren in der GUI mit 0-3 nummeriert. Ist das ein Problem? Falls ja, wer passt sich an? 
- Fenster für Infos: Gregor hat zwei Fenster für Informationen für die Clients geplant. In einem Fenster (rechts oben), sollen allgemeine Informationen vom Server angezeigt werden, z.B. "INFO beginner is". Im Fenster unten links sollen einerseits clientspezifische Informationen angezeigt werden, z.B. "Invalid card", oder Informationen, die lediglich für uns während des Entwickelns nötig sind, z.B. wenn ein Thread abgestürzt ist. 
- Javadoc: Ruben machte uns darauf aufmerksam, dass die Dokumentation mittels Javadoc angepasst werden muss, wenn eine Methode überarbeitet wird und beispielsweise andere Parameter enthält. 
- HighScore: Der HighScore soll in einem XML-File gespeichert werden. Wir diskutierten mehrere Möglichkeiten, einen HighScore aufzustellen, wie z.B. die Anzahl gefressener Spielfiguren, Anzahl SURR, Anzahl Runden. Zuerst soll im HighScore jedoch nur vermerkt werden, ob eine Person resp. ein Team gewonnen hat. 
- SendFromServer Absturz: Es besteht das Problem, dass SendFromServer manchmal abstürzt, wenn ein Spiel beendet wird. Eventuell kann dieses Problem mit einer Blocking Queue oder mit wait() und notify() gelöst werden. 

Bis Mittwoch kümmert sich Gregor um das flackernde Fenster und SURR, Ruben implementiert den TeamMode, Victory und HighScore und Johanna versucht das Problem mit SendFromServer zu lösen und erste Unit-Tests zu schreiben. 


**Nächstes Treffen:** Mittwoch, 28.04.21, 10:15 Uhr

---

*29.04.2021, 10 Uhr, Johanna*

### Protokoll#17 - Protokoll

Gestern Nachmittag und heute Morgen gingen wir gemeinsam den Code von Server und Client und den dazugehörigen Klassen durch, damit alle den Ablauf besser verstehen. Wir besprachen bestehende Probleme und Lösungsvorschläge. 
- SendFromServer Absturz: Dieses Problem könnte wohl mit einer Blocking Queue gelöst werden. Die Behebung dieses Fehlverhaltens scheint uns aber nicht prioritär, da wir uns zuerst um die Meilenstein-Achievements sowie einen übersichtlicheren Code kümmern wollen. 

Bis zum nächsten Treffen bearbeiten wir folgende Aufgaben:
- Gregor: GUI vervollständigen, so dass der Chat und die Log-Infos angezeigt werden. Herausfinden, wieso ein OpenGame nicht gelöscht wird, obwohl es eigentlich von der Liste der AllGamesNotFinished entfernt wird. Hintergrundbild für das Spielbrett (falls mit wenig Aufwand möglich). 
- Ruben: Victory und TeamMode. Falls die Zeit reicht, XML-File erstellen, um die Spielstatistiken zu speichern. 
- Johanna: EXIT und QUIT vereinheitlichen. JUnit-Tests für GameLogic. 

Ruben machte uns darauf aufmerksam, dass die Javadoc-Kommentare vollständig und korrekt sein müssen und man sich doch darauf achten soll, wenn man den Code bearbeitet. 

**Nächstes Treffen:** Montag, 03.05.21, 09:00 Uhr

---
03.05.2021, 9 Uhr, Johanna

### Protokoll#18 - Protokoll

In der heutigen Sitzung besprachen wir die gemachten Änderungen. Es mussten einige Dinge im Code angepasst werden, da beispielsweise nun immer über das MainGame auf die Informationen zum Spiel zugegriffen wird und nicht mehr direkt über den GameState oder das GameFile. Zudem hat Gregor BlockingQueues eingebaut. Bezüglich GUI denken wir, dass es schwierig ist, einen dynamischen Hintergrund zu erstellen. Bis zur Übungsstunde am Mittwoch sind die folgenden Aufgaben zu erledigen:
- Ruben stellt den Spezialfall Seven fertig, so dass jede gültige Kombination mit der Karte Seven gespielt werden kann. 
- Gregor stellt das Vicotry-Fenster im GUI dar und behebt einen Darstellungsfehler (die Zuordnung Spieler - Farbe war auf dem Spielbrett vertauscht).
- Johanna kümmert sich weiter um die JUnit-Tests.
- Ruben fragt bei Tim nach, ob die Library, die er für die Erstellung der HighScore-XML-Datei verwenden möchte, verwendet werden darf.
- Alle testen das Spiel, indem sie es v.a. im TeamMode spielen. 

**Nächstes Treffen:** Mittwoch, 05.05.21, 10:15 Uhr

---

04.05.2021, 15:30 Uhr, Johanna

### Protokoll#19 - Protokoll

Ruben und ich trafen uns heute mit Tim, um über die Unit-Tests zu sprechen. Wir hatten das Problem, dass wir nicht genau wussten, wie wir unseren Code testen können, da wir z.B. den Status des Spielbretts nicht überblicken können und die Player eine ServerConnection verlangen. Tim meinte, wir sollten im RuleCheck die Regelüberprüfung von den Nachrichten, die an die Clients gesendet werden, trennen. Die Methoden im RulesCheck sollten nicht void sein, sondern etwas an ServerGameCommand zurückgeben. Aus dem ServerGameCommand heraus kann dann eine Nachricht an die Clients gesendet werden. Dann könnten wir die Regelüberprüfung isoliert testen. Zudem schlug er vor, das Spielbrett auch auf Serverseite als Array darzustellen, sodass wir jederzeit einen Überblick über das ganze Spielbrett haben und es auch für Testzwecke gezielt manipulieren können. 
Wir haben uns nun für folgendes Vorgehen entschieden: Aus dem RulesCheck werden alle ServerConnections entfernt. Die Regelüberprüfungsmethoden geben einen Integer-Wert an ServerGameCommand zurück. Wir können das Spielbrett zum Testen herrichten, indem wir die einzelnen Figuren gezielt auf ein Feld setzen. So sollte es möglich sein, Unit-Tests zu schreiben. 

**Nächstes Treffen:** Mittwoch, 05.05.21, 10:15 Uhr

---

05.05.2021, 10:15 Uhr, Johanna

### Protokoll#20 - Protokoll

Wir haben in der Übungsstunde folgende Punkte besprochen:
- Die ServerConnections sind nun aus dem RulesCheck entfernt. Jedoch müssen sie auch noch aus dem RulesCheckHelper entfernt werden.
- CSV: Gregor hat ein Grundgerüst erstellt, um die Spielstatistik in einem CSV zu speichern. Dieses Dokument soll jeweils aktualisiert werden, wenn ein Spiel beendet wurde, weil es einen Gewinner gab (und nicht, weil das Spiel abgebrochen wurde).
- TeamMode: Wenn ein neues Game erstellt wird, sollte man per Button auswählen können, ob man den TeamMode aktivieren möchte oder nicht. 
- "Wrong format": Wenn man ein Spiel mit TeamModus eröffnet wurde, können sich zwei Spieler problemlos anschliessen. Wenn der vierte Spieler "Join" klickt, erscheint die Meldung "wrong format", wenn er es noch einmal versucht, ist er dann beim Spiel dabei. 
- SEVE: Ruben hat den Spezialfall SEVEN soweit abgeschlossen, er muss nun noch ausführlich getestet werden. 

**Nächstes Treffen:** Donnerstag, 06.05.21, 10:00 Uhr: Test über Hamachi


---

06.05.2021, 10:00 Uhr, Johanna

### Protokoll#21 - Protokoll

Beim Test über Hamachi traten einige seltsame Dinge auf, die wir zuvor nicht bemerkt haben und auch nicht reproduzieren konnten. Z.B. wurde eine gefressene Kugel nicht nach Hause sondern auf ein anderes Feld auf dem Track geschickt. Bis zum Treffen am Freitag wollen wir weitere Fehler finden und beheben. 

**Nächstes Treffen:** Freitag, 07.05.21, 10:00 Uhr

---

07.05.2021, 10:00 Uhr, Johanna

### Protokoll#22 - Protokoll

Heute testeten wir das Spiel wieder zu dritt und behoben verschiedene kleine Fehler. Z.B. bestand dass Problem, dass ein Spieler, der in einen falschen Himmel gehen wollte, danach komplett blockiert war. Bis morgen sollen die folgenden Aufgabe erledigt sein:
- Problem mit SEVE: Es gibt ein Problem, wenn man die Karte 7 spielen möchte, aber zuerst einen falschen MOVE angibt. Das hängt damit zusammen, dass der tempGameState auf den GameState übertragen wird. 
- Javadoc: Wir bearbeiten und vervollständigen die Javadoc-Kommentare auf einem eigenen Branch.
- Organisation: Jeder sollte seine nicht mehr benötigten Branches löschen.
- SURR: Wenn man SURR gespielt hat, sollte man keine Karten mehr auswählen können. 
- Darstellung der Karten: Momentan ist es so, dass ab der zweiten Runde alle Karten verblasst dargestellt werden und dadurch nicht mehr erkennbar ist, welche Karten noch spielbar sind.
- Spielfelder einfärben: Um das Spielfedl übersichtlicher zu gestalten, wollen wir die Startfelder jeweils farblich hervorheben. 
- Logfile: Bei jedem Serverstart sollte ein neues Logfile erstellt werden. 
- Spielanleitung: Die Spielanleitung muss auf das Spielen in der GUI angepasst werden. 

**Nächstes Treffen:** Samstag, 08.05.21, 14:00 Uhr

---

08.05.2021, 14:00 Uhr, Johanna

### Protokoll#23 - Protokoll

Beim Testen des Spiels stellten wir fest, dass es noch einige Probleme mit der Spezialkarte 7 gab. Denn bei der Karte 7 wird ein temporäres Spielbrett erstellt, um die Bewegungen zu überprüfen. Die neuen Positionen müssen dann wieder korrekt an das richtige Spielfeld übergeben werden. Ein technisches Problem entstand bei Hamachi. Wenn Gregor einen Server startete, konnten sich die anderen Teammitglieder nicht mit diesem Server verbinden. Wenn eine andere Person den Server startete, konnten sich alle damit verbinden. Dieses Problem konnten wir nicht lösen. Wir entdeckten und behoben weitere kleine Fehler. 

**Nächstes Treffen:** Samstag, 09.05.21, 16:00 Uhr

--- 

09.05.2021, 16:00 Uhr, Johanna

### Protokoll#24 - Protokoll

Als wir heute versuchten, ein jar zu erstellen, funktionierte es nicht. Es gab ein Problem mit der CSV-Datei, in der wir die Gewinnerstatistiken speichern wollten. Wir versuchten lange, dieses Problem zu lösen. Schliesslich konnten wir eine jar Datei erstellen, die wir sogleich über Hamachi testeten. 

**Nächstes Treffen:** Dienstag, 11.05.21, 15:30 Uhr

--- 

11.05.2021, 15:30 Uhr, Johanna

### Protokoll#25 - Protokoll

Wir testeten gemeinsam die Spiele der Gruppen 12 und 14, Ruben verfasste das Feedback. Danach gingen wir die Achievements für Meilenstein 5 durch und besprachen folgende Punkte:
- Software Architektur: Ruben erstellt bis am Freitag ein Modell. 
- Design: Wir wollen das Spiel in braun und beige gestalten, sodass es zum Hund passt, den Gregor gezeichnet hat. 
- HighScore: Die HighScore-Liste muss in einem CSV-File gespeichert werden und aufgerufen werden können. 
- Protokolldokument: Das Dokument muss aktualisiert werden, sobald die letzten zusätzlichen Befehle implementiert sind. 
- Karten austauschen zu Beginn: Diese Funktion wollten wir eigentlich einbauen, hat aber keine hohe Priorität.
- Nur eine Karte ablegen: Auch diesen Punkt werden wir nur bearbeiten, falls die Zeit reicht. 
- Audio: Es kam die Idee auf, Hundegebell oder einen anderen passenden Sound zu hinterlegen. Dies hat jedoch keine hohe Priorität. 
- Sir Nicholas: Uns ist nicht ganz klar, was mit diesem Malus gemeint ist. 
- Zweite Library: Wir müssen eine zweite Library finden, die wir sinnvoll in unserem Projekt einbauen können. 
- HighScore-Liste: Die HighScore-Liste soll in der Lobby in einem Nebenfenster angezeigt werden.
- Help: Der Help-Button soll auf die Spielanleitung verweisen. 


Bis Freitag sind die folgenden Aufgaben zu erledigen:
- Modell der Software-Architektur erstellen
- Protokoll: Statt Strings die Enums verwenden
- Infomeldungen mit dem Befehl "FAIL" versenden 
- CSV für HighScore
- Lobby neu gestalten
- Hundebild und Schrift integrieren
- QA-Messungen durchführen und Verbesserungsvorschläge einbringen

**Nächstes Treffen:** Mittwoch, 12.05.21, 10:15 Uhr

**Übernächstes Treffen:** Freitag, 14.05.21, 10:00 Uhr
