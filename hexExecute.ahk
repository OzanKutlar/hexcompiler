; 1366 resolution
dist := 75

; 1920 resolution 
; dist := 106

; You can configure to your display by getting your horizontal resolution and dividing it by 18
delayTime := 200
sqrt3 := 1.7320

NumpadAdd::
	Clipwait, 1
	Loop, 9
	{
		left()
	}
	for i, hex in StrSplit(Trim(Clipboard), ",")
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
			case "ps":
				KeyWait, NumpadAdd, D
				Loop, 9
				{
					left()
				}
			case "cn":
				MouseMove, A_ScreenWidth/2, A_ScreenHeight/2

			default:
				MsgBox, % "Error on hex no " i " which is " hex
		}
	}
	MsgBox, Spell Finished
return

Numpad6::
	Click, down
	right()
	up(0)
	left()
	down(0)
	down(1)
	right()
	up(1)
	Click, up
	right()
	right()
	up(1)
	Click, down
	down(1)
	left()
	up(0)
	down(0)
	down(1)
	right()

	Click, up
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