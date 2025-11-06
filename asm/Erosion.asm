LD R4, #21 									;0
LD R3, #0 									;1

;PAD 0-20
STORE_OFFSET, R4, R3, #400 	;2
SUB, R4, R4, #1 						;3
BRnzp, 001, #2 							;4

;PAD 39-40, 59-60... 359-360
LD R4, #18 									;5
LD R2, #39 									;6
STORE_OFFSET, R2, R3, #400 	;7
STORE_OFFSET, R2, R3, #401 	;8
ADD R2, R2, #20 						;9
SUB, R4, R4, #1							;10
BRnzp, 001, #7 							;11

;PAD 379-399
LD R4, #21 									;12
STORE_OFFSET, R4, R3, #779 	;13
SUB R4, R4, #1 							;14
BRnzp, 001, #13 						;15

;EROSION INITIALIZATION
LD 1, R4 #18  							;16
LD 1, R2 #20  							;17

;Y LOOP
LD 1, R5 #18  							;18
ADD 1, R2, R2, #2  					;19

;X LOOP

;PIXEL+1
LD 0, R7, R2  							;20
ADD 1, R2, R2, #19  				;21

;PIXEL+20
LD 0, R6, R2  							;22
AND R7, R7, R6  						;23
SUB R2, R2, #40  						;24

;PIXEL-20
LD 0, R6, R2  							;25
AND R7, R7, R6  						;26
ADD 1, R2, R2, #19  				;27

;PIXEL-1
LD 0, R6, R2  							;28
AND R7, R7, R6  						;29
ADD 1 R2, R2, #3  					;30

;WRITE
STORE_OFFSET R2, R7, #400		;31

;X LOOP CHECKUP
SUB R5, R5, #1 							;32
BRnzp 001, #20     					;33

;Y LOOP CHECKUP
SUB R4, R4, #1							;34
BRnzp 001,  #18    					;35

END  												;36
