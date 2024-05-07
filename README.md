# Radibautz
Kartenspiel
Entwicklung des 2D-Kartenspiels «Radibautz» mit JAVA

# Ausgangslage / Beschreibung der Anforderung
Im Fach Objektorientierte Programmieren werden die Studenten ein 2D Spiel in Java entwickeln. Für dieses Projekt möchte ich gerne das Kartenspiel Radibautz entwickeln. Die Spielregeln sind folgende:

Zu Beginn wird zufällig ein Dealer bestimmt, der jedem Spieler und in die Mitte drei Kar-ten austeilt. Der Dealer hat einmalig die Möglichkeit, seine drei Karten durch neue auszu-tauschen. Ist der Dealer zufrieden mit seinen Karten, können die Spieler nacheinander im Uhrzeigersinn ihre Karten mit den Karten in der Mitte tauschen. Es ist erlaubt, entwe-der eine Karte oder direkt alle drei Karten zu tauschen. Der Austausch von zwei Karten ist nicht möglich. Das Ziel des Spiels ist es, eine möglichst hohe Punktzahl zu erreichen. Um die Punktzahl zu ermitteln, werden die Karten in der Hand zusammengezählt. Es dür-fen nur Karten mit dem gleichen Symbol zusammengezählt werden, oder bei drei Karten mit dem gleichen Wert (Beispiel: 6, 6, 6) hat man die Punktzahl 30,5.

Wenn ein Ass zusammen mit zwei Karten mit einem Buchstaben oder einer 10 auf der Hand ist, hat man automatisch einen Radibautz und das Spiel ist gewonnen. Sobald ein Radibautz erreicht wird, muss dies sofort mitgeteilt werden, sonst hat man verloren. Der Gewinner darf dann den nächsten Dealer bestimmen (bei Verwendung als Trinkspiel muss die ausgewählte Person ausserdem einen Kurzen trinken). Das Spiel kann auch enden, wenn nach der ersten Runde jemand zufrieden mit seinen Karten ist und durch Klopfen auf den Tisch signalisiert, dass er aufhört. Sobald jemand geklopft hat, geht das Spiel noch eine Runde weiter, bis zur Person, die geklopft hat, und dann werden die Kar-ten gezeigt.

Das französische und das schweizerische Kartenset können verwendet werden. Karten mit einem Wert unter 10 werden nicht berücksichtigt, da dies die Wahrscheinlichkeit für einen Radibautz deutlich verringert.

# Akzeptanzkriterien
•	Zufällige Elemente (Verteilung der Karten) ist vorhanden
•	Ein Spiel kann bis zu 4 Computergegner enthalten.
•	Über Konfigdatei kann das Kartenset definiert werden (Franz / Schweiz)
•	Dokumentation mittels JavaDoc
•	Statistik über die Spielverläufe ist einsehbar

# Abgrenzung
Das Spiel ist ausschliesslich offline verfügbar. Einen Mehrspielermodus über das Netz-werk ist in der ersten Version nicht geplant.

