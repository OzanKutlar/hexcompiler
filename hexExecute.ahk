dist := 106
delayTime := 20
sqrt3 := 1.7320

; InputBox, hexToExecute, % "Please input a compiled hex.", % "The Script will then wait for NumpadAdd to be pressed."
; hexToExecute := "r,r,r,r,c1,dr,l,ul,dl,dr,r,c0,ur,ul,r,r"
; spells := StrSplit(hexToExecute, " ")
; StringSplit, spells, hexToExecute, " "
; MsgBox, % spells " : " hexToExecute " : " StrSplit(hexToExecute)
NumpadAdd::
	Clipwait, 1
	for i, hex in StrSplit(Clipboard, ",")
	{
		; MsgBox, % i " : " hex
		switch hex
		{
			case "dl":
				down(0)
			case "dr":
				down(1)
			case "ul":
				up(0)
			case "ur":
				up(1)
				
			case "l":
				left()

			case "r":
				right()

			case "c0":
				Click, up

			case "c1":
				Click, down

			default:
				MsgBox, % "Error on hex no " i " which is " hex
		}
	}
return


up(right){
	global dist
	global delayTime
	global sqrt3

	if(right){
		MouseMove, % (dist/2), % -(dist/2)*sqrt3, 1, R
	}
	else{
		MouseMove, % -(dist/2), % -(dist/2)*sqrt3, 1, R
	}
	sleep, % delayTime
}
down(right){
	global dist
	global delayTime
	global sqrt3

	if(right){
		MouseMove, % (dist/2), % (dist/2)*sqrt3, 1, R
	}
	else{
		MouseMove, % -(dist/2), % (dist/2)*sqrt3, 1, R
	}
	sleep, % delayTime
}
right(){
	global dist
	global delayTime
	global sqrt3

	MouseMove, % dist, 0, 1, R
	sleep, % delayTime
}
left(){
	global dist
	global delayTime
	global sqrt3

	MouseMove, % -(dist), 0, 1, R
	sleep, % delayTime
}

RCtrl::Reload
RShift::ExitApp