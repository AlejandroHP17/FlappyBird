extends Node2D

const SAVE_FILE: String = "user://score.save"

@onready var spawner: Spawner = $Spawner
@onready var player: Player = $Player
@onready var game_ui: GameUI = $GameUI

var score: int
var highscore: int

func _ready() -> void:
	load_highscore()


func save_highscore() -> void:
	if score > highscore:
		highscore = score
		var file = FileAccess.open(SAVE_FILE, FileAccess.WRITE)
		file.store_32(highscore)


func load_highscore() -> void:
	var file = FileAccess.open(SAVE_FILE, FileAccess.READ)
	if file:
		highscore = file.get_32()


func _on_player_on_game_started() -> void:
	spawner.timer.start()
	game_ui.start_menu.hide()


func _on_spawner_on_obstacle_crash() -> void:
	player.stop_movement()


func _on_ground_on_player_crash() -> void:
	spawner.stop_obstacles()
	game_ui.calculate_score(score, highscore)
	game_ui.game_over()


func _on_spawner_on_player_score() -> void:
	score += 1
	game_ui.update_score(score)
	save_highscore()
