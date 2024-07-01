# Radibautz
Kartenspiel
Entwicklung des 2D-Kartenspiels «Radibautz» mit JAVA

# Ausgangslage / Beschreibung der Anforderung
Im Fach Objektorientierte Programmieren werden die Studenten ein 2D Spiel in Java entwickeln. Für dieses Projekt möchte ich gerne das Kartenspiel Radibautz entwickeln. Die Spielregeln sind folgende:

Zu Beginn wird zufällig ein Dealer bestimmt, der jedem Spieler und in die Mitte drei Kar-ten austeilt. Der Dealer hat einmalig die Möglichkeit, seine drei Karten durch neue auszu-tauschen. Ist der Dealer zufrieden mit seinen Karten, können die Spieler nacheinander im Uhrzeigersinn ihre Karten mit den Karten in der Mitte tauschen. Es ist erlaubt, entwe-der eine Karte oder direkt alle drei Karten zu tauschen. Der Austausch von zwei Karten ist nicht möglich. Das Ziel des Spiels ist es, eine möglichst hohe Punktzahl zu erreichen. Um die Punktzahl zu ermitteln, werden die Karten in der Hand zusammengezählt. Es dür-fen nur Karten mit dem gleichen Symbol zusammengezählt werden, oder bei drei Karten mit dem gleichen Wert (Beispiel: 6, 6, 6) hat man die Punktzahl 30,5.

Wenn ein Ass zusammen mit zwei Karten mit einem Buchstaben oder einer 10 auf der Hand ist, hat man automatisch einen Radibautz und das Spiel ist gewonnen. Sobald ein Radibautz erreicht wird, muss dies sofort mitgeteilt werden, sonst hat man verloren. Der Gewinner darf dann den nächsten Dealer bestimmen (bei Verwendung als Trinkspiel muss die ausgewählte Person ausserdem einen Kurzen trinken). Das Spiel kann auch enden, wenn nach der ersten Runde jemand zufrieden mit seinen Karten ist und durch Klopfen auf den Tisch signalisiert, dass er aufhört. Sobald jemand geklopft hat, geht das Spiel noch eine Runde weiter, bis zur Person, die geklopft hat, und dann werden die Kar-ten gezeigt.

Das französische und das schweizerische Kartenset können verwendet werden. Karten mit einem Wert unter 10 werden nicht berücksichtigt, da dies die Wahrscheinlichkeit für einen Radibautz deutlich verringert.

# Abgrenzung
Das Spiel ist ausschliesslich offline verfügbar. Einen Mehrspielermodus über das Netz-werk ist in der ersten Version nicht geplant.


# Anweisungen wie das Programm erstellt und gestartet wird
**Voraussetzungen**
Git: Stellen Sie sicher, dass Git auf Ihrem Computer installiert ist. Git herunterladen
Java Development Kit (JDK): Stellen Sie sicher, dass das JDK installiert ist.
IDE: Es wird empfohlen, eine integrierte Entwicklungsumgebung (IDE) wie IntelliJ IDEA, Eclipse oder NetBeans zu verwenden.

**1. Repository von GitHub-klonen:**
  git clone <URL-Ihres-GitHub-Repositorys>
  cd <Name-des-geklonten-Verzeichnisses>

**2. Projekt in IDE öffnen:**
   IntelliJ:
    - Wählen Sie Open und navigieren Sie zu dem Verzeichnis, das Sie geklont haben, und öffnen Sie es.
    - IntelliJ sollte das Projekt automatisch als Java-Projekt erkennen und die entsprechenden Einstellungen vornehmen.

**3. Abhängigkeiten und Konfiguration:**
Stellen Sie sicher, dass das Projekt korrekt konfiguriert ist und alle notwendigen Abhängigkeiten aufgelöst sind. In den meisten Fällen sollte Ihre IDE dies automatisch tun.

**4. Projekt erstellen und ausführen:**
  - Wählen Sie im Projektfenster die Main-Klasse aus.
  - Klicken Sie mit der rechten Maustaste darauf und wählen Sie Run 'Main.main()'

# Grundlegende Angaben
**Spielstart**

**Programm starten:**
Starten Sie das Programm, indem Sie die Main-Klasse im Package DisplayCards ausführen. Dies öffnet das Hauptfenster des Spiels.
Benutzeroberfläche
Die Benutzeroberfläche besteht aus verschiedenen Bereichen:

**Spieler-1-Bereich (unten):** Zeigt die Karten von Spieler 1 und enthält Aktionsbuttons.
**Spieler-2-Bereich (links):** Zeigt die Karten von Spieler 2.
**Spieler-3-Bereich (rechts):** Zeigt die Karten von Spieler 3.
**Board-Bereich (oben):** Zeigt die Karten auf dem Board.
**Statusbereich (Mitte):** Zeigt den aktuellen Status des Spiels, den aktuellen Spieler, die Runde und ggf. welcher Spieler geklopft hat.

**Spielablauf**
**Karten anzeigen:**

Zu Beginn des Spiels werden die Karten für alle Spieler und das Board gemischt und verteilt.
Zug eines Spielers:

**Aktionen von Spieler 1**

**Karte tauschen:** Klicken Sie auf eine Karte von Spieler 1, um sie auszuwählen. Klicken Sie dann auf eine Karte auf dem Board, um die beiden Karten zu tauschen.
**Alle Karten tauschen:** Klicken Sie auf den Button "Swap All", um alle Karten von Spieler 1 mit den Karten auf dem Board zu tauschen.
**Radibautz überprüfen:** Klicken Sie auf den Button "Radibautz", um zu überprüfen, ob die Punktzahl 31 erreicht wurde. Wenn ja, gewinnt der Spieler sofort.
**Klopfen:** Klicken Sie auf den Button "Klopfen", wenn Sie mit Ihren Karten zufrieden sind. Klopfen ist nur ab der zweiten Runde möglich.
**Spieler überspringen:** Klicken Sie auf den Button "SkipPlayer", um den Zug zu beenden, ohne eine Aktion durchzuführen. Dies ist nur möglich, wenn in der ersten Runde eine gültige Aktion durchgeführt wurde. (Nur zum debuggen)

**Aktionen der KI (Spieler 2 und 3):**

Die KI entscheidet automatisch, ob sie eine Karte oder alle Karten tauscht oder klopft, basierend auf der Punktzahl.
Rundenwechsel:

Nach jeder Aktion wechselt der Zug zum nächsten Spieler.
Die aktuelle Runde wird erhöht, wenn Spieler 1 an der Reihe ist und niemand geklopft hat.
Spielende:

Das Spiel endet, wenn ein Spieler geklopft hat und alle anderen Spieler ihren Zug beendet haben.
Ein Popup zeigt den Gewinner mit der höchsten Punktzahl und den Spieler mit der niedrigsten Punktzahl an.
Beispielspiel
Spieler 1 ist an der Reihe:

Wählen Sie eine Karte von Spieler 1 und klicken Sie auf eine Karte auf dem Board, um sie zu tauschen.
Alternativ können Sie den Button "Swap All" verwenden, um alle Karten zu tauschen.
Klicken Sie auf den Button "Radibautz", um zu überprüfen, ob die Punktzahl 31 erreicht wurde.
Wenn Sie mit Ihren Karten zufrieden sind, klicken Sie auf "Klopfen".
Um den Zug ohne Aktion zu beenden, klicken Sie auf "SkipPlayer" (nur nach einer gültigen Aktion in der ersten Runde möglich).
Spieler 2 (KI) ist an der Reihe:

Die KI analysiert die Karten und entscheidet, ob sie eine Karte tauscht, alle Karten tauscht oder klopft.
Der Zug wechselt automatisch zu Spieler 3.
Spieler 3 (KI) ist an der Reihe:

Die KI analysiert die Karten und entscheidet, ob sie eine Karte tauscht, alle Karten tauscht oder klopft.
Der Zug wechselt automatisch zurück zu Spieler 1.
Punkteberechnung
Kartenwerte:
Zahlenkarten (6 bis 10) haben ihren jeweiligen Wert.
Bildkarten (Bube, Dame, König) haben den Wert 10.
Ass hat den Wert 11.
Sonderfall:
Wenn alle drei Karten die gleiche Zahl, aber unterschiedliche Farben haben, beträgt die Punktzahl 30.5.
