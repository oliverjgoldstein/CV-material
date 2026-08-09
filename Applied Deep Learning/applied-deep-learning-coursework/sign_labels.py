
sign_types = ["Speed Limits", "Other Prohibitory", "Derestriction Signs", "Mandatory Signs", "Danger Signs", "Unique Signs"]

sign_values = [[0,1,2,3,4,5,6,7,8],[9,10,15,16],[6,32,41,42],[33,34,35,36,37,38,39,40],[11,18,19,20,21,22,23,24,25,26,27,28,29,30,31],[12,13,14,17]]

def getSignIndex(sign):
    global sign_values
    for idx, val in enumerate(sign_values):
        if sign in val:
            return idx
