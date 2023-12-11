import argparse


def printLabyrinth(lab):
    pass


def suchen(zeile: int, spalte: int, lab):
    if lab[zeile][spalte] == 'A':
        return True
    elif lab[zeile][spalte] == '#' or lab[zeile][spalte] == 'x':
        return False

    lab[zeile][spalte] = 'x'

    printLabyrinth(lab)
    # System.out.println()
    #
    # Thread.sleep(1000)

    return (suchen(zeile, spalte + 1, lab) or   # rechts
            suchen(zeile, spalte - 1, lab) or   # links
            suchen(zeile - 1, spalte, lab) or   # oben
            suchen(zeile + 1, spalte, lab))     # unten


def parse_args():
    """
    The necessary commands to make the command line tools usable
    :return:
    """

    parser = argparse.ArgumentParser()
    parser.add_argument("filename")
    parser.add_argument("-x", "--xstart", help="x-coordinate to start", action="store_true")
    parser.add_argument("-y", "--ystart", help="y-coordinate to start", action="store_true")
    parser.add_argument('-p', "--print", help="print output of every solution", action="store_true")
    parser.add_argument("-t", "--time", help="print total calculation time (in milliseconds)", action="store_true")
    parser.add_argument("-d", "--delaay", help="delay after printing a solution (in milliseconds)", action="store_true")

    args = parser.parse_args()

    if args.filename:
        pass


if __name__ == '__main__':
    parse_args()
    print("done")
