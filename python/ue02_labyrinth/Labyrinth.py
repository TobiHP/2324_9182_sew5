import argparse
from time import sleep

delay = 0
do_print = False


def print_labyrinth(lab: [str]):
    for row in lab:
        print(row.strip())


def suchen(zeile: int, spalte: int, lab: [[]]):
    if do_print:
        print_labyrinth(lab)
        print()

    if lab[zeile][spalte] == 'A':
        return True
    elif lab[zeile][spalte] == '#' or lab[zeile][spalte] == 'x':
        return False

    lab[zeile] = lab[zeile][:spalte] + 'x' + lab[zeile][spalte + 1:]

    sleep(delay)

    return (suchen(zeile, spalte + 1, lab) or  # rechts
            suchen(zeile, spalte - 1, lab) or  # links
            suchen(zeile - 1, spalte, lab) or  # oben
            suchen(zeile + 1, spalte, lab))  # unten


def suchen_alle(zeile: int, spalte: int, lab: [[]]):
    anz_wege = 0

    if lab[zeile][spalte] == 'A':
        return 1
    elif lab[zeile][spalte] != ' ':
        return 0

    lab[zeile] = lab[zeile][:spalte] + 'x' + lab[zeile][spalte + 1:]

    if do_print:
        print_labyrinth(lab)
        print()

    anz_wege += (suchen_alle(zeile, spalte + 1, lab) +  # rechts
                 suchen_alle(zeile, spalte - 1, lab) +  # links
                 suchen_alle(zeile + 1, spalte, lab) +  # oben
                 suchen_alle(zeile - 1, spalte, lab))  # unten

    lab[zeile] = lab[zeile][:spalte] + ' ' + lab[zeile][spalte + 1:]

    return anz_wege


def read_file(filename):
    with open(filename, "r") as f:
        return f.readlines()


def parse_args():
    """
    The necessary commands to make the command line tools usable
    :return:
    """
    global do_print
    global delay

    parser = argparse.ArgumentParser()
    parser.add_argument("filename")
    parser.add_argument("-x", "--xstart", help="x-coordinate to start", action="store_true")
    parser.add_argument("-y", "--ystart", help="y-coordinate to start", action="store_true")
    parser.add_argument('-p', "--print", help="print output of every solution", action="store_true")
    parser.add_argument("-t", "--time", help="print total calculation time (in milliseconds)", action="store_true")
    parser.add_argument("-d", "--delay", help="delay after printing a solution (in milliseconds)", action="store_true")

    args = parser.parse_args()

    if args.filename:
        lab = read_file(args.filename)
        do_print = args.print
        delay = args.delay
        xstart = args.xstart if args.xstart else 1
        ystart = args.ystart if args.ystart else 1
        print(suchen_alle(xstart, ystart, lab))


if __name__ == '__main__':
    parse_args()
    print("done")
