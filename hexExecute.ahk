compile("ur,ul,dl,dr")

compile(spells*){
	totalLength := 0
	for i, spell in spells
	{
		spellLength = calculateSpellLength(i, spell)
		MsgBox, % spellLength.left " " spellLength.right
	}
}

calculateSpellLength(i,spell){
	spellLengthMax := 0
	spellLengthMin := 0
	spellLength := 0
	spellComponents := StrSplit(spell, ",")
	; MsgBox, % spellComponents
	for ii, comp in spellComponents
	{	
		; MsgBox, test
		spellOld := spellLength
		posL := InStr(comp, "l")
		if(posL != 0){
			if(posL == 1){
				spellLength -= 1
			}
			if(posL == 2){
				spellLength -= 0.5
			}
		}
		posR := InStr(comp, "r")
		if(posR != 0){
			if(posR == 1){
				spellLength += 1
			}
			if(posR == 2){
				spellLength += 0.5
			}
		}
		if(spellOld == spellLength){
			MsgBox, % "Error on spell " i " component " ii
			return
		}
		if(spellLength > spellLengthMax){
			spellLengthMax := spellLength
		}
		
		if(spellLength < spellLengthMin){
			spellLengthMin := spellLength
		}
	}
	spellLength := []
	return spellLength
	; MsgBox, % "Size of spell " i " is : " spellLength " with it going right " spellLengthMax " and left " spellLengthMin
}

RCtrl::Reload