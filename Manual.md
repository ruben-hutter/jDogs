#  Manual JDogs
Willkommen bei JDogs! Hier erfährst du alles rund um das Spiel JDogs. JDogs ist eine Adaption von Brändidog, einem bekannten Brettspiel, bei welchem Karten eine wichtige Rolle spielen. Das Ziel des Spiels ist es, mit den eigenen vier Spielfiguren als erster ins Ziel zu gelangen. Dabei bestimmen die Karten, wie weit man pro Zug laufen kann. 

## Spiel starten
- Um den Server zu starten, führe die jar-Datei mit den Argumenten "server port" aus. 
- Um einen Client zu starten, führe die jar-Datei mit den Argumenten "client hostadress:port" aus. 
- Nun bist du in der Public Lobby. Dir wurde ein Default-Nickname zugeteilt. Wie du diesen ändern kannst und welche weiteren Aktionen du in der Lobby durchführen kannst, wird in untenstehendem Abschnitt beschrieben. 

## In der Public Lobby
- **Nickname ändern**: Um deinen Nickname zu ändern, tippe "USER gewünschterName" ein, z.B. "USER max".
- **Alle Spieler anzeigen**: Wenn du wissen möchtest, wer alles mit dem Server verbunden ist, dann schreib "ACTI". 
- **Nachricht an alle senden**: Wenn du eine Nachricht an alle anderen Spieler in der Public-Lobby senden möchtest, schreib "PCHT Nachricht", z.B. "PCHT Hallo zusammen".
- **Nachricht nur an eine Person senden** : Wenn du eine Nachricht nur an eine Person senden möchtest, dann musst du "WCHT NameDerPerson Nachricht" schreiben, also z.B. "WCHT anna Hallo Anna", wenn du nur Anna begrüssen möchtest. 
- **Spiele anzeigen**: Mit dem Befehl "SESS" kannst du dir die Spiele anzeigen lassen, denen du beitreten kannst.
- **Einem Spiel beitreten** : Wenn du einem Spiel beitreten möchtest, schreibe "JOIN Spielname", also z.B. "JOIN game", wenn du dem Spiel mit dem Namen "game" beitreten möchtest. 
- **Spiel eröffnen**: Wenn du selbst ein Spiel starten möchtest, kannst du das mittels des Befehls "OGAM spielname anzahlSpieler Teams" tun, also z.B. "OGAM game 4 0", wenn du mit vier Spielern und ohne Teams spielen möchtest. JDogs kann momentan nur von 4 Spielern und ohne Teams gespielt werden. 
- **Das Spiel starten** : Sobald genügend Mitspieler gefunden wurden, kann derjenige Spieler, der das Spiel mittels "OGAM" eröffnet hat, das Spiel starten. Schreibe dazu den Befehl "STAR". Nun siehst du das Spielbrett, die Kugeln und deine Karten. 

## JDogs spielen
- **Spielbeginn**: Wenn ein Spiel mittels des Befehls "STAR" eröffnet wurde, wird ein zufälliger Spieler als Beginner ausgewählt. Alle Mitspieler werden informiert, wer den ersten Zug machen darf. Der Beginner sieht die Information "Your turn". 
- **Farben**: Allen Mitspielern wird eine der folgenden Farben zugewiesen: Gelb (YELO), Grün (GREN), Blau (BLUE), Rot (REDD). Die vier Spielfiguren einer Farbe sind nummeriert. Wenn du z.B. rot bist, dann heissen deine Figuren "REDD-1", "REDD-2", "REDD-3", "REDD-4". 
- **Aus dem Zwinger gehen**: Um aus dem Zwinger auf den Track zu gehen, benötigst du eine der folgenden Karten: König, Ass, Joker. Hast du eine dieser Karten auf der Hand, kannst du mit folgendem Befehl aus dem Zwinger gehen: "MOVE KARTE Spielfigur Zielposition". 
  - Startposition GELB: B00
  - Startposition GRÜN: B16
  - Startposition BLAU: B32
  - Startposition ROT: B48
- Wenn du eine Spielfigur aus dem Zwinger gebracht hast, kannst du dich auf dem Track fortbewegen. Dazu musst du jeweils den folgenden Befehl eingeben "MOVE Karte Spielfigur Zielposition", also z.B. "MOVE THRE YELO-1 B06", wenn du die gelbe Figur mit der Karte Drei auf Feld B06 bringen möchtest. 
- **Aussetzen**: Wenn du keine Karte spielen kannst, schreibe den Befehl "MOVE SURR".
- **Ins Ziel gelangen**: Die Felder im Ziel beginnen mit dem Buchstaben "C".

## Spielanleitung JDogs
- Das Ziel ist es, die eigenen Spielfiguren aus dem Zwinger(Home) ins Ziel (Heaven) zu bringen. Dazu musst du zuerst aus dem Zwinger auf den Track und schliesslich ins Ziel. Um dich auf dem Spielbrett fortzubewegen, dienen dir die Karten.
- Du siehst nur deine eigenen Karten. Du darfst den anderen Spielern nicht mitteilen, welche Karten du hast.
- Es wird der Reihe nach gespielt. Wer dran ist, sieht bei sich die Aufforderung "Your turn"
- Bei jedem Zug musst du eine Karte spielen, wenn du kannst. 
- Wenn du mit deiner Murmel auf ein Feld kommst, das bereits von einer anderen Murmel besetzt ist, muss diejenige Murmel, die zuerst da war, zurück in den Zwinger. 
- Um ins Ziel zu gelangen, musst du den eigenen Start mindestens zwei Mal betreten haben. Das Ziel muss von innen nach aussen aufgefüllt werden. 
- Gewonnnen hat der Spieler, der als erster alle vier Spielfiguren ins Ziel gebracht hat. 

**Kartenwerte**

Hier siehst du, welche Kürzel für welche Karten stehen, welche Aktionen du mit dieser Karte durchführen kannst und wie du sie als Befehl eingeben musst, falls es Besonderheiten gibt. 
  - ACEE: Ass: Start, 1 oder 11 Schritte vorwärts. "MOVE ACE1" (1 Schritt), "MOVE AC11" (11 Schritte)
  - KING: König: Start oder 13 Schritte vorwärts.
  - JOKE: Joker: Start oder Einsatz nach Wunsch. "MOVE JOKE KING", wenn du die Karte Joker als König spielen möchtest. 
  - JACK: Junge: Murmel mit einer von einem Mitspieler tauschen.
  - SEVE: Karte 7: 7 Schritte vorwärts, beliebig aufteilbar auf die eigenen Murmeln. "MOVE AnzahlKugeln Kartenwert1 Spielfigur1 Zielposition1 Kartenwert2 Spielfigur2 Zielposition2", also z.B. "MOVE SEVE 2 FIVE YELO-1 B23 TWOO YELO-2 B04", wenn du mit der Karte 7 die Spielfigur YELO-1 um 5 Felder und die Spielfigur YELO-2 um 2 Felder nach vorne bewegen möchtest. 
  - FOUR: Karte 4: 4 Schritte vor- oder rückwärts.
  - QUEN: Dame: 12 Schritte vorwärts.
  - TENN: Karte 10: 10 Schritte vorwärts.
  - NINE: Karte 9: 9 Schritte vorwärts.
  - EIGT: Karte 8: 8 Schritte vorwärts.
  - SIXX: Karte 6: 6 Schritte vorwärts.
  - FIVE: Karte 5: 5 Schritte vorwärts.
  - THRE: Karte 3: 3 Schritte vorwärts.
  - TWOO: Karte 2: 2 Schritte vorwärts.


