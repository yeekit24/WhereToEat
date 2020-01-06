from flask import Flask
from flask import request, json, jsonify
import mysql.connector
from datetime import datetime
'''
mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    passwd="",
    database="wte"
)

'''
mydb = mysql.connector.connect(
    host="localhost",
    user="ploywide",
    passwd="ploywide321",
    database="wte"
)


app = Flask(__name__)



@app.route('/')
def hello_world():
    return 'Hello Worldss!'


@app.route('/connect', methods=['POST'])
def init_connection():
    if 'device_guid' in request.json and 'login_device_type_id' in request.json:
        mycursor = mydb.cursor()
        mycursor.execute("SELECT user_id, user_device_id FROM user_device WHERE device_guid = %s and login_device_type_id = %s and active_status = 1",(request.json["device_guid"],request.json["login_device_type_id"]))
        myresult = mycursor.fetchall()

        if len(myresult) > 0:
            mycursor.close()
            return jsonify(
                error_code=0,
                error_status="",
                return_data={
                    "user_id": myresult[0][0],
                    "user_device_id": myresult[0][1],
                }
            )
        else:
            insert_user_query = (
                "INSERT INTO user_info("
                "username, api_key, active_status, modified_by, modified_time) VALUES ("
                "%s, %s, %s, %s, %s)"
            )
            mycursor.execute(insert_user_query, ("", None, 1, 1, datetime.now()))
            user_id = mycursor.lastrowid
            insert_device_query = (
                "INSERT INTO user_device(user_id, login_device_type_id, device_guid, active_status,modified_by, modified_time) VALUES (%s, %s, %s, %s, %s, %s)")
            mycursor.execute(insert_device_query, (user_id, request.json['login_device_type_id'], request.json['device_guid'],1, user_id, datetime.now()))
            mydb.commit()
            user_device_id = mycursor.lastrowid
            mycursor.close()
            return jsonify(
                error_code = 0,
                error_status = "",
                return_data = {
                    "user_id": user_id,
                    "user_device_id": user_device_id
                }
            )

    return jsonify(
        error_code = -1,
        error_status = "error request",
        return_data = {}
    )


@app.route('/choices', methods =['GET'])
def get_choice():
    if 'user_id' in request.args:
        user_id = request.args['user_id']
        mycursor = mydb.cursor()
        mycursor.execute("SELECT * FROM choices WHERE user_id = %s AND active_status = 1", (user_id,))
        myresult = mycursor.fetchall()
        mycursor.close()
        return jsonify(
            error_code=0,
            error_status="",
            return_data={
                'choices': myresult
            }
        )
    else:
        return jsonify(
            error_code=-1,
            error_status="error request",
            return_data={
            }
        )


@app.route('/choices/create', methods =['POST'])
def create_choice():
    if 'choice_name' in request.json and 'user_id' in request.json:
        if "choice_desc" not in request.json:
            request.json["choice_desc"] = ""

        mycursor = mydb.cursor()
        insert_choice_query = (
            "INSERT INTO choices("
            "choice_name, choice_desc, user_id, active_status, modified_by, modified_time) VALUES ("
            "%s, %s, %s, %s, %s, %s)"
        )
        mycursor.execute(insert_choice_query, (request.json["choice_name"], request.json["choice_desc"],  request.json["user_id"], 1, 1, datetime.now()))
        choice_id = mycursor.lastrowid
        mydb.commit()
        mycursor.close()
        return jsonify(
            error_code=0,
            error_status="",
            return_data={
                "choice_id": choice_id,
            }
        )
    else:
        return jsonify(
            error_code=-1,
            error_status="invalid request",
            return_data={
            }
        )


@app.route('/choices/<id>', methods=['DELETE'])
def delete_choice(id):

    try:
        mycursor = mydb.cursor()
        update_choice_query = (
            "UPDATE choices SET active_status = 0, modified_time = %s WHERE choice_id = %s"
        )
        mycursor.execute(update_choice_query, (datetime.now(), id))
        mydb.commit();
        mycursor.close()
        return jsonify(
            error_code=0,
            error_status="",
            return_data={
            }
        )
    except mysql.connector.Error as err:
        return jsonify(
            error_code=err.errno,
            error_status=err.msg,
            return_data={
            }
        )


@app.route('/choices/<id>', methods =['PUT'])
def update_choice():
    pass


@app.route('/spin', methods =['POST'])
def spin():
    if 'selected_choice_id' in request.json and 'user_id' in request.json:
        mycursor = mydb.cursor()
        mycursor.execute(
            "INSERT INTO spin_action(selected_choice_id, active_status,modified_by, modified_time) VALUES (%s, %s, %s, %s)",
            (request.json['selected_choice_id'], 1, request.json['user_id'], datetime.now()))
        mydb.commit()
        spin_action_id = mycursor.lastrowid
        mycursor.close()
        return jsonify(
            error_code=0,
            error_status="",
            return_data={
                'spin_action': spin_action_id
            }
        )
    return jsonify(
        error_code=-1,
        error_status="invalid request",
        return_data={
        }
    )


@app.route('/choice_profile', methods =['GET'])
def get_choice_profile():
    if 'user_id' in request.json:
        mycursor = mydb.cursor()
        mycursor.execute("SELECT * FROM choice_profile WHERE user_id = %s AND active_status = 1",
                         (request.json['user_id'],))
        myresult = mycursor.fetchall()
        mycursor.close()
        return jsonify(
            error_code=0,
            error_status="",
            return_data={
                'choice_profile': myresult
            }
        )
    else:
        return jsonify(
            error_code=-1,
            error_status="error request",
            return_data={
            }
        )


@app.route('/choice_profile/create', methods =['POST'])
def create_choice_profile():
    if 'choice_profile_name' in request.json and 'user_id' in request.json and 'longtitude' in request.json and 'latitude' in request.json:
        if "choice_profile_desc" not in request.json:
            request.json["choice_profile_desc"] = ""

        mycursor = mydb.cursor()
        insert_choice_query = (
            "INSERT INTO choice_profile("
            "choice_profile_name, choice_profile_desc, longtitude, latitude, user_id, active_status, modified_by, modified_time) VALUES ("
            "%s, %s, %s, %s, %s, %s, %s, %s)"
        )
        mycursor.execute(insert_choice_query, (
        request.json["choice_profile_name"], request.json["choice_profile_desc"], request.json["longtitude"], request.json["latitude"], request.json["user_id"], 1, 1, datetime.now()))
        choice_profile_id = mycursor.lastrowid
        mydb.commit()
        mycursor.close()
        return jsonify(
            error_code=0,
            error_status="",
            return_data={
                "choice_profile_id": choice_profile_id,
            }
        )
    else:
        return jsonify(
            error_code=-1,
            error_status="invalid request",
            return_data={
            }
        )


@app.route('/choice_profile/{id}', methods =['DELETE'])
def delete_choice_profile():
    pass


@app.route('/choices_reference/{id}', methods =['PUT'])
def update_choice_profile():
    pass


if __name__ == '__main__':
    app.run('0.0.0.0')
