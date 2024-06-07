package com.bieldev.dwe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RiskActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk)

        // Passos do Trabalgo checkboxes
        val checkBoxVehicleDisplacement = findViewById<CheckBox>(R.id.checkBox_vehicle_displacement)
        val checkBoxSignalingOnTheRoad = findViewById<CheckBox>(R.id.checkBox_signaling_on_the_road)
        val checkBoxToolMobilization = findViewById<CheckBox>(R.id.checkBox_tool_mobilization)
        val checkBoxManualExcavationWithTools = findViewById<CheckBox>(R.id.checkBox_manual_excavation_with_tools)
        val checkBoxHdInstallation = findViewById<CheckBox>(R.id.checkBox_hd_installation)
        val checkBoxPipeInstallation = findViewById<CheckBox>(R.id.checkBox_pipe_installation)
        val checkBoxBackfilling = findViewById<CheckBox>(R.id.checkBox_backfilling)
        val checkBoxDemobilizationCleaning = findViewById<CheckBox>(R.id.checkBox_demobilization_cleaning)
        val checkBoxMaterialRemoval = findViewById<CheckBox>(R.id.checkBox_material_removal)
        val checkBoxVehicleDisplacementEnd = findViewById<CheckBox>(R.id.checkBox_vehicle_displacement_end)

        // Risk checkboxes
        val checkBoxSprainedAnkle = findViewById<CheckBox>(R.id.checkBox_sprained_ankle)
        val checkBoxFallingObject = findViewById<CheckBox>(R.id.checkBox_falling_object)
        val checkBoxFallOnSameLevel = findViewById<CheckBox>(R.id.checkBox_fall_on_same_level)
        val checkBoxOpenSpace = findViewById<CheckBox>(R.id.checkBox_open_space)
        val checkBoxFallFromHeight = findViewById<CheckBox>(R.id.checkBox_fall_from_height)
        val checkBoxParticleInEye = findViewById<CheckBox>(R.id.checkBox_particle_in_eye)
        val checkBoxDustExposure = findViewById<CheckBox>(R.id.checkBox_dust_exposure)
        val checkBoxOutdoorWork = findViewById<CheckBox>(R.id.checkBox_outdoor_work)
        val checkBoxHandDislocation = findViewById<CheckBox>(R.id.checkBox_hand_dislocation)
        val checkBoxGasExposure = findViewById<CheckBox>(R.id.checkBox_gas_exposure)
        val checkBoxNoiseExposure = findViewById<CheckBox>(R.id.checkBox_noise_exposure)
        val checkBoxHumidityExposure = findViewById<CheckBox>(R.id.checkBox_humidity_exposure)
        val checkBoxCut = findViewById<CheckBox>(R.id.checkBox_cut)
        val checkBoxVehicleCollision = findViewById<CheckBox>(R.id.checkBox_vehicle_collision)
        val checkBoxPedestrianAccident = findViewById<CheckBox>(R.id.checkBox_pedestrian_accident)
        val checkBoxCollision = findViewById<CheckBox>(R.id.checkBox_collision)

        // Nivel checkboxes
        val checkBoxHigh = findViewById<CheckBox>(R.id.checkBox_high)
        val checkBoxMedium = findViewById<CheckBox>(R.id.checkBox_medium)
        val checkBoxLow = findViewById<CheckBox>(R.id.checkBox_low)

        //Barreiras de Proteção
        val checkBoxSpeedLimit = findViewById<CheckBox>(R.id.checkBox_speed_limit)
        val checkBoxVehicleBarrier = findViewById<CheckBox>(R.id.checkBox_vehicle_barrier)
        val checkBoxSignage = findViewById<CheckBox>(R.id.checkBox_signage)
        val checkBoxGroundLevel = findViewById<CheckBox>(R.id.checkBox_ground_level)
        val checkBoxOrganizedTools = findViewById<CheckBox>(R.id.checkBox_organized_tools)
        val checkBoxTechnicalProbe = findViewById<CheckBox>(R.id.checkBox_technical_probe)
        val checkBoxSunscreen = findViewById<CheckBox>(R.id.checkBox_sunscreen)
        val checkBoxSpeedLimit2 = findViewById<CheckBox>(R.id.checkBox_speed_limit_2)
        val checkBoxNotApplicable = findViewById<CheckBox>(R.id.checkBox_not_applicable)



        val editTextRisk = findViewById<EditText>(R.id.edit_text_risk)
        val buttonSubmit = findViewById<Button>(R.id.button_submit)

        buttonSubmit.setOnClickListener {
            val riskText = editTextRisk.text.toString()
            val vehicleDisplacementSelected = checkBoxVehicleDisplacement.isChecked
            val signalingOnTheRoadSelected = checkBoxSignalingOnTheRoad.isChecked
            val toolMobilizationSelected = checkBoxToolMobilization.isChecked
            val manualExcavationWithToolsSelected = checkBoxManualExcavationWithTools.isChecked
            val hdInstallationSelected = checkBoxHdInstallation.isChecked
            val pipeInstallationSelected = checkBoxPipeInstallation.isChecked
            val backfillingSelected = checkBoxBackfilling.isChecked
            val demobilizationCleaningSelected = checkBoxDemobilizationCleaning.isChecked
            val materialRemovalSelected = checkBoxMaterialRemoval.isChecked
            val vehicleDisplacementEndSelected = checkBoxVehicleDisplacementEnd.isChecked
            val checkBoxSpeedLimitSelected = checkBoxSpeedLimit.isChecked
            val checkBoxVehicleBarrierSelected = checkBoxVehicleBarrier.isChecked
            val checkBoxSignageSelected = checkBoxSignage.isChecked
            val checkBoxGroundLevelSelected = checkBoxGroundLevel.isChecked
            val checkBoxOrganizedToolsSelected = checkBoxOrganizedTools.isChecked
            val checkBoxTechnicalProbeSelected = checkBoxTechnicalProbe.isChecked
            val checkBoxSunscreenChecked = checkBoxSunscreen.isChecked
            val checkBoxSpeedLimit2Selected = checkBoxSpeedLimit2.isChecked
            val checkBoxNotApplicableSelected = checkBoxNotApplicable.isChecked
            val checkBoxSafetyBeltSelected = findViewById<CheckBox>(R.id.checkBox_safety_belt).isChecked
            val checkBoxSonicAlarmSelected = findViewById<CheckBox>(R.id.checkBox_sonic_alarm).isChecked
            val checkBoxSafetyGlovesSelected = findViewById<CheckBox>(R.id.checkBox_safety_gloves).isChecked
            val checkBoxSafetyBootsSelected = findViewById<CheckBox>(R.id.checkBox_safety_boots).isChecked
            val checkBoxHelmetWithJugularSelected = findViewById<CheckBox>(R.id.checkBox_helmet_with_jugular).isChecked
            val checkBoxSunscreenSelected = findViewById<CheckBox>(R.id.checkBox_sunscreen).isChecked
            val checkBoxSafetyGlassesSelected = findViewById<CheckBox>(R.id.checkBox_safety_glasses).isChecked
            val checkBoxMaskPFF2Selected = findViewById<CheckBox>(R.id.checkBox_mask_pff2).isChecked
            val checkBoxTechnicalControlSelected = findViewById<CheckBox>(R.id.checkBox_technical_control).isChecked
            val checkBoxEarProtectorSelected = findViewById<CheckBox>(R.id.checkBox_ear_protector).isChecked
            val checkBoxPvcGlovesBootsSelected = findViewById<CheckBox>(R.id.checkBox_pvc_gloves_boots).isChecked
            val checkBoxUniformArabTouqueSelected = findViewById<CheckBox>(R.id.checkBox_uniform_arab_touque).isChecked
            val checkBoxVaquetaGlovesSelected = findViewById<CheckBox>(R.id.checkBox_vaqueta_gloves).isChecked
            val checkBoxUniformSelected = findViewById<CheckBox>(R.id.checkBox_uniform).isChecked


            // Passos do Trabalho Selecionados
            val selectedOptions = StringBuilder()
            if (vehicleDisplacementSelected) selectedOptions.append("Deslocamento com Veículo, ")
            if (signalingOnTheRoadSelected) selectedOptions.append("Sinalização na via para Execução de Atividade, ")
            if (toolMobilizationSelected) selectedOptions.append("Mobilização de Ferramentas, ")
            if (manualExcavationWithToolsSelected) selectedOptions.append("Escavação Manual com Ferramentas, ")
            if (hdInstallationSelected) selectedOptions.append("Instalação de HD, ")
            if (pipeInstallationSelected) selectedOptions.append("Assentamento de Tubo, ")
            if (backfillingSelected) selectedOptions.append("Reaterro, ")
            if (demobilizationCleaningSelected) selectedOptions.append("Desmobilização/Limpeza, ")
            if (materialRemovalSelected) selectedOptions.append("Retirada de Material, ")
            if (vehicleDisplacementEndSelected) selectedOptions.append("Deslocamento com Veículo (Final), ")


            // Riscos Selecionados
            val selectedRisks = mutableListOf<String>()
            if (checkBoxVehicleCollision.isChecked) selectedRisks.add("Colisão de Veículos")
            if (checkBoxPedestrianAccident.isChecked) selectedRisks.add("Atropelamento")
            if (checkBoxCollision.isChecked) selectedRisks.add("Colisão")
            if (checkBoxSprainedAnkle.isChecked) selectedRisks.add("Torção no Pé")
            if (checkBoxFallingObject.isChecked) selectedRisks.add("Queda de Objeto")
            if (checkBoxFallOnSameLevel.isChecked) selectedRisks.add("Queda em Nível")
            if (checkBoxOpenSpace.isChecked) selectedRisks.add("Espaço a Céu Aberto")
            if (checkBoxFallFromHeight.isChecked) selectedRisks.add("Queda em Altura")
            if (checkBoxParticleInEye.isChecked) selectedRisks.add("Partícula nos Olhos")
            if (checkBoxDustExposure.isChecked) selectedRisks.add("Exposição a Poeira")
            if (checkBoxOutdoorWork.isChecked) selectedRisks.add("Trabalho a Céu Aberto")
            if (checkBoxHandDislocation.isChecked) selectedRisks.add("Luxação na Mão")
            if (checkBoxGasExposure.isChecked) selectedRisks.add("Gás")
            if (checkBoxNoiseExposure.isChecked) selectedRisks.add("Ruído")
            if (checkBoxHumidityExposure.isChecked) selectedRisks.add("Umidade")
            if (checkBoxCut.isChecked) selectedRisks.add("Corte")

            // Nivel de Risco
            val selectedNivel = mutableListOf<String>()
            if (checkBoxHigh.isChecked) selectedNivel.add("Alto")
            if (checkBoxMedium.isChecked) selectedNivel.add("Médio")
            if (checkBoxLow.isChecked) selectedNivel.add("Baixo")

            // Barreiras de Controle
            val selectedControlBarriers = mutableListOf<String>()
            if (checkBoxSpeedLimitSelected) selectedControlBarriers.add("Limite de Velocidade da Via")
            if (checkBoxVehicleBarrierSelected) selectedControlBarriers.add("Veículo Barreira")
            if (checkBoxSignageSelected) selectedControlBarriers.add("Placas de Sinalização")
            if (checkBoxGroundLevelSelected) selectedControlBarriers.add("Atenção ao Desnível do Solo")
            if (checkBoxOrganizedToolsSelected) selectedControlBarriers.add("Ferramentas Organizadas")
            if (checkBoxTechnicalProbeSelected) selectedControlBarriers.add("Sondagem Técnica")
            if (checkBoxSunscreenSelected) selectedControlBarriers.add("Protetor Solar")
            if (checkBoxSpeedLimit2Selected) selectedControlBarriers.add("Limite de Velocidade Da Via")
            if (checkBoxNotApplicableSelected) selectedControlBarriers.add("Não Aplicável")

            //Barreiras de Proteção
            val selectedProtectionBarriers = mutableListOf<String>()
            if (checkBoxSafetyBeltSelected) selectedProtectionBarriers.add("Cinto de Segurança")
            if (checkBoxSonicAlarmSelected) selectedProtectionBarriers.add("Alarme Sonoro")
            if (checkBoxSafetyGlovesSelected) selectedProtectionBarriers.add("Luva de Segurança")
            if (checkBoxSafetyBootsSelected) selectedProtectionBarriers.add("Bota de Segurança")
            if (checkBoxHelmetWithJugularSelected) selectedProtectionBarriers.add("Capacete com Jugular")
            if (checkBoxSunscreenSelected) selectedProtectionBarriers.add("Protetor Solar")
            if (checkBoxSafetyGlassesSelected) selectedProtectionBarriers.add("Óculos de Segurança")
            if (checkBoxMaskPFF2Selected) selectedProtectionBarriers.add("Mascara PFF2")
            if (checkBoxTechnicalControlSelected) selectedProtectionBarriers.add("Controle Técnico")
            if (checkBoxEarProtectorSelected) selectedProtectionBarriers.add("Protetor Auricular")
            if (checkBoxPvcGlovesBootsSelected) selectedProtectionBarriers.add("Luva e Bota de PVC")
            if (checkBoxUniformArabTouqueSelected) selectedProtectionBarriers.add("Uniforme/Touca Arabe")
            if (checkBoxVaquetaGlovesSelected) selectedProtectionBarriers.add("Luva Vaqueta")
            if (checkBoxUniformSelected) selectedProtectionBarriers.add("Uniforme")

            // Remove the trailing comma and space if any
            if (selectedOptions.isNotEmpty()) {
                selectedOptions.delete(selectedOptions.length - 2, selectedOptions.length)
            }

            val toastMessage = "Passos do Trabalho: $selectedOptions\nRiscos selecionados: ${selectedRisks.joinToString(", ")}"
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()

            // Imprimir no Logcat
            Log.d("RiskActivity", toastMessage)
        }

    }
}