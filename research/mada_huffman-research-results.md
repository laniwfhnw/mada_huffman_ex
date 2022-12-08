---
theme: uncover
paginate: true
backgroundColor: #fff
backgroundImage: url('https://marp.app/assets/hero-background.svg')
footer: Bianca Christen-Piekenbrock, Julien Kellerhals, Lani Wagner
style: |
  h2 {
    text-align: left;
    margin-bottom: 1em;
  }
---

<!--
_class: lead
_paginate: false
-->

# **mada Huffman Analyse**

Analyse Effizienz und Sparpotenzial<br><br>

---

## Daten

* Pangramm für das englische Alphabet
* "Hello World!"
* Langer _Lorem Ipsum_ text

---

## Vergleich

|                  | Pangramm | Hello World | Lorem Ipsum |
|------------------|---------:|------------:|------------:|
| Klartext         |      44B |         12B |      2'420B |
| Kodierungsschema |     288B |         71B |        717B |
| Kodierter Text   |      26B |          5B |      1'381B |
| Gesamt Kodierung |     314B |         76B |      2'098B |

---

## Auswertung Pangramm

* Kodierungsschema gross, aufgrund der gewählten Speicherform und des Texts.
* Kodierter Text hingegen etwas kleiner als Klartext.
* Gesamte Kodierungsgrösse ~7 Mal grösser.

---

## Auswertung Hello World

* Kodierungsschema gross, aufgrund der gewählten Speicherform und des kurzen Texts.
* Kodierter Text weniger als halb so gross wie Klartext.
* Gesamte Kodierungsgrösse ~6 Mal grösser

---

## Auswertung Lorem Ipsum

* Kodierungsschema gross, aber kleiner ales Text selbst.
* Kodierter Text etwas mehr als halb so gross wie Klartext.
* Gesamte Kodierungsgrösse ~15% kleiner.

---

## Schlussfolgerung

Bei genug langen Texten fängt diese Implementation der Hoffman-Kodierung sich auszuzahlen. Es liesse sich aber sicher noch mehr Speicher sparen durch optimierte Speicherung des Kodierungsschemas.