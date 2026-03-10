#!/bin/zsh

# Function to map the full 3-char sequence to ADB
send_key() {
    case "$1" in
        $'\e[A') adb shell input keyevent 19 && echo "↑ UP" ;;
        $'\e[B') adb shell input keyevent 20 && echo "↓ DOWN" ;;
        $'\e[D') adb shell input keyevent 21 && echo "← LEFT" ;;
        $'\e[C') adb shell input keyevent 22 && echo "→ RIGHT" ;;
        $'\n')   adb shell input keyevent 66 && echo "↵ ENTER" ;; # Added Enter
        $'\e')   return ;; # Ignore stray escapes
    esac
}

echo "Listening... (Arrows to move, Enter to select, 'q' to quit)"

# Save terminal state and turn off echoing/line buffering
stty_orig=$(stty -g)
stty -icanon -echo

while true; do
    # Read 1 character
    read -k 1 char

    if [[ "$char" == "q" ]]; then
        break
    elif [[ "$char" == $'\e' ]]; then
        # If it's an escape, grab the next 2 chars immediately
        # -t 0.1 prevents hanging if it was just a lonely Escape key
        read -k 2 -t 0.1 rest
        send_key "${char}${rest}"
    elif [[ "$char" == $'\n' ]]; then
        send_key "$char"
    fi
done

# Clean up terminal
stty "$stty_orig"
echo "\nExited."